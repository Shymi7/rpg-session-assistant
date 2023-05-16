import {Section} from "./Section";
import {Text, View} from "react-native";
import React from "react";
import {Btn} from "./Btn";

interface Props {
    name: string;
    description: string;
    level: number;
    experience: number;
    GMMode?: boolean;
    showCharacterListFunc?: () => void;
}

export function CharacterInfo({name, description, level, experience, GMMode = false, showCharacterListFunc}: Props) {

    return (
        <Section colorVariant={'dark'}>
            <Text className={'font-bold text-3xl text-center text-color-white my-2'}>
                {name + ' (' + level + ')'}
            </Text>

            {
                GMMode && showCharacterListFunc &&
                <Btn
                    func={showCharacterListFunc}
                    iconIndex={0}
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
        </Section>
    );
}




