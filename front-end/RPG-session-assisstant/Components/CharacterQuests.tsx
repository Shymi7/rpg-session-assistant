import {Section} from "./Section";
import {Text, TouchableOpacity, View} from "react-native";
import React from "react";

interface Quest {
    description: string;
    id: number;
    name: string;
}

interface Props {
    questsList: Quest[];
}

export function CharacterQuests({questsList}: Props) {

    const questElements = questsList.map((quest: Quest, index:number) => {
        return (
            <View className={'w-full'} key={index}>
                {
                    index !== 0 &&
                    <View className={'w-full bg-color-accent rounded h-1 my-3'}></View>
                }
                <Text className={'text-color-white text-xl font-bold'}>
                    {quest.name}
                </Text>
                <Text className={'text-color-white text-l'}>
                    {quest.description}
                </Text>
            </View>
        );
    })


    return (
        <Section colorVariant={'dark'} title={'Quests'}>
            <View className={''}>
                {questElements}
            </View>
        </Section>
    );
}




