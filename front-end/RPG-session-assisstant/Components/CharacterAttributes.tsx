import {Section} from "./Section";
import {Text, TouchableOpacity, View} from "react-native";
import React from "react";

interface Attribute {
    attribute: {
        id: number;
        name: string;
    };
    attributeLevel: number;
}

interface Props {
    attributesList: Attribute[];
    freeSkillPoints: number;
    refreshFunc: () => void;
}

export function CharacterAttributes({attributesList, freeSkillPoints, refreshFunc}: Props) {

    function sendAttributeLvlUpRequest(){

    }


    const attributeElements = attributesList.map((attribute: Attribute) => {
        return (
            <View className={'w-full flex-row my-2'} key={attribute.attribute.id}>
                <View className={'w-5/6 mr-2'}>
                    <Section colorVariant={"dark"} hasPadding={false} hasMargin={false}>
                        <View className={'flex-row justify-between py-2 px-4'}>
                            <Text className={'text-xl text-color-white'}>
                                {attribute.attribute.name}
                            </Text>
                            <Text className={'text-xl text-color-accent'}>
                                {attribute.attributeLevel}
                            </Text>
                        </View>

                    </Section>
                </View>
                <TouchableOpacity
                    className={'bg-color-accent rounded-2xl p-2 w-12 justify-center '}
                    onPress={() => {
                        sendAttributeLvlUpRequest();
                        refreshFunc();
                        //todo send attributeLvlUp request
                    }}
                >
                    <Text className={'text-xl text-color-white font-bold text-center'}>
                        +
                    </Text>
                </TouchableOpacity>

            </View>
        );
    })


    return (
        <Section colorVariant={'light'} title={'Attributes'}>
            <View className={''}>
                {attributeElements}
            </View>
        </Section>
    );
}




