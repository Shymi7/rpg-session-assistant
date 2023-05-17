import {Section} from "./Section";
import {Text, View} from "react-native";
import React from "react";
import {Btn} from "./Btn";
import {CustomInput} from "./CustomInput";

interface Props {
    name: string;
    description: string;
    level: number;
    experience: number;
    GMMode?: boolean;
    showCharacterListFunc?: () => void;
}

export function CharacterInfo({name, description, level, experience, GMMode = false, showCharacterListFunc}: Props) {

    function sendAddXpRequest(){

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
                        func={()=>{}}
                    />
                    <Btn
                        func={sendAddXpRequest}
                        text={'+'}
                        additionalTailwindClasses={'w-10 ml-4'}
                    />
                </View>

            }


        </Section>
    );
}




