import {Section} from "./Section";
import {Text, View} from "react-native";
import React, {useState} from "react";
import {CustomInput} from "./CustomInput";
import {Btn} from "./Btn";
import {
    getUserDataFromLocalStorage,
    isArrayFilledWithTrue,
    modifyElementInArrayByIndex,
    requestWithAuthKey
} from "../utils/utils";
import {API_URL} from "../env";
import classNames from "classnames";

interface Quest {
    description: string;
    id: number;
    name: string;

}

interface Props {
    questsList: Quest[];
    GMMode?: boolean;
    characterId?: number;
    refreshFunc?: () => void;
}


export function CharacterQuests({questsList, GMMode = false, characterId, refreshFunc}: Props) {

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(2));

    const [questNameInputValue, setQuestNameInputValue] = useState<string>('');
    const [questDescriptionInputValue, setQuestDescriptionInputValue] = useState<string>('');

    async function sendCreateNewQuestRequest() {
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            //create new quest
            const createNewQuestUrl = API_URL + '/api/quest/create';
            const createNewQuestBody = {
                name: questNameInputValue,
                description: questDescriptionInputValue
            }
            const res = await requestWithAuthKey(createNewQuestUrl, authKey, "POST", createNewQuestBody);
            const idOfNewQuest = res.data.id;

            //assign that quest to character
            const addQuestToCharacterUrl = API_URL + '/api/character/add-quest';
            const addQuestToCharacterBody = {
                characterId: characterId,
                entityId: idOfNewQuest
            }
            await requestWithAuthKey(addQuestToCharacterUrl, authKey, "PATCH", addQuestToCharacterBody);

            if (refreshFunc) {
                refreshFunc();
            }

        } catch (error) {
            console.log('create new quest request error: ' + error);
        }
    }

    async function sendRemoveQuestRequest(questId: number) {
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            const addQuestToCharacterUrl = API_URL + '/api/character/remove-quest';
            const addQuestToCharacterBody = {
                characterId: characterId,
                entityId: questId
            }
            await requestWithAuthKey(addQuestToCharacterUrl, authKey, "PATCH", addQuestToCharacterBody);

            if (refreshFunc) {
                refreshFunc();
            }
        } catch (error) {
            console.log('remove quest from character request error: ' + error);
        }
    }

    const questElements = questsList
        .sort((a, b) => a.id - b.id)
        .map((quest: Quest, index: number) => {
            return (
                <View className={'w-full'} key={quest.id}>
                    {
                        index !== 0 &&
                        <View className={'w-full bg-color-accent rounded h-1 my-3'}></View>
                    }
                    <View className={'flex-row'}>
                        <View className={
                            classNames(GMMode ? 'w-5/6' : 'w-full')
                        }>
                            <Text className={'text-color-white text-xl font-bold'}>
                                {quest.name}
                            </Text>
                            <Text className={'text-color-white text-l'}>
                                {quest.description}
                            </Text>
                        </View>
                        {
                            GMMode &&
                            <Btn
                                func={() => sendRemoveQuestRequest(quest.id)}
                                iconIndex={3}
                                additionalTailwindClasses={'w-10 h-10'}
                            />
                        }

                    </View>

                </View>
            );
        })

    return (
        <Section colorVariant={'dark'} title={'Quests'}>
            {
                GMMode &&
                <View className={'flex-col mb-4'}>
                    <CustomInput
                        placeholder={'Quest title'}
                        regex={/^.{3,30}$/}
                        func={(value: string, isValid: boolean) => {
                            setQuestNameInputValue(value);
                            setAreInputsValid(prevState =>
                                modifyElementInArrayByIndex(prevState, 0, isValid)
                            )
                        }}
                    />
                    <CustomInput
                        placeholder={'Quest description'}
                        regex={/^.{4,100}$/}
                        func={(value: string, isValid: boolean) => {

                            setQuestDescriptionInputValue(value);
                            setAreInputsValid(prevState =>
                                modifyElementInArrayByIndex(prevState, 1, isValid)
                            )
                        }}
                    />
                    <Btn
                        func={() => {
                            sendCreateNewQuestRequest().catch(e => console.log(e));
                        }}
                        text={'Add quest'}
                        additionalTailwindClasses={'self-center'}
                        disabled={!isArrayFilledWithTrue(areInputsValid)}
                    />
                </View>
            }
            <View className={''}>
                {questElements}
            </View>
        </Section>
    );
}




