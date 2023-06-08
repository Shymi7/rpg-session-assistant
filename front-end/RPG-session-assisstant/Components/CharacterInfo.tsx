import {Section} from "./Section";
import {Text, View} from "react-native";
import React from "react";
import {Btn} from "./Btn";
import {CustomInput} from "./CustomInput";
import {getUserDataFromLocalStorage, requestWithAuthKey} from "../utils/utils";
import {API_URL} from "../env";

interface Props {
    name: string;
    description: string;
    level: number;
    experience: number;
    refreshFunc: () => void;
    characterId: number;
    roomID: number;
    GMMode?: boolean;
    showCharacterListFunc?: () => void;


}

export function CharacterInfo({
                                  name,
                                  description,
                                  level,
                                  experience,
                                  GMMode = false,
                                  showCharacterListFunc,
                                  refreshFunc,
                                  characterId,
                                  roomID
                              }: Props) {

    const [xpInputValue, setXpInputValue] = React.useState('');
    const [isInputValid, setIsInputValid] = React.useState(false)

    async function sendAddXpRequest() {
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            const addXpRequestUrl = API_URL + '/api/character/add-xp';
            const addXpRequestBody = {
                characterId: characterId,
                roomId: roomID,
                experience: xpInputValue
            }

            await requestWithAuthKey(addXpRequestUrl, authKey, "PATCH", addXpRequestBody);

            if (refreshFunc) {
                refreshFunc();
            }
        } catch (error) {
            console.log('modify attributes request error: ' + error);
        }
    }

    return (
        <Section colorVariant={'dark'}>
            <Text className={'font-bold text-3xl text-center text-color-white my-2'}>
                {name + ' (' + level + ')'}
            </Text>

            {
                GMMode && showCharacterListFunc &&
                <Btn
                    func={showCharacterListFunc}
                    iconIndex={2}
                    additionalTailwindClasses={'w-10 absolute self-end right-3 top-3'}
                />
            }

            <Text className={'text-lg text-color-white'}>
                {description}
            </Text>

            <Section colorVariant={'light'} hasPadding={false}>
                <View className={'h-11 justify-center'}>
                    <View
                        className={'rounded-2xl absolute bg-color-accent p-2 h-full'}
                        //css style instead of tailwind only to create experience bar
                        style={{width: String(experience) + '%'}}
                    ></View>

                    <Text className={'text-xl text-color-white text-center'}>
                        {String(100 - experience) + '% left to ' + (level + 1) + 'lvl'}
                    </Text>
                </View>

            </Section>

            {
                GMMode &&
                <View className={'flex-row pl-4 pr-2'}>
                    <CustomInput
                        placeholder={'Add XP (as %)'}
                        regex={/^([1-9]|[1-9][0-9]|100)$/}
                        func={(value: string, isValid: boolean) => {
                            setXpInputValue(value);
                            setIsInputValid(isValid);
                        }}
                    />
                    <Btn
                        func={sendAddXpRequest}
                        text={'+'}
                        additionalTailwindClasses={'w-10 ml-4'}
                        disabled={!isInputValid}
                    />
                </View>

            }


        </Section>
    );
}




