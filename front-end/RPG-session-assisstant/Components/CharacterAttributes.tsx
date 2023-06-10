import {Section} from "./Section";
import {Text, TouchableOpacity, View} from "react-native";
import React from "react";
import {getUserDataFromLocalStorage, requestWithAuthKey} from "../utils/utils";
import {API_URL} from "../env";
import classNames from "classnames";

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
    GMMode?: boolean;
    characterId?: number;

}

export function CharacterAttributes({
                                        attributesList,
                                        freeSkillPoints,
                                        refreshFunc,
                                        GMMode = false,
                                        characterId
                                    }: Props) {

    async function sendAttributeLvlUpRequest(attributeId: number, addedValue = 1) {
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            const modifyAttributeUrl = API_URL + '/api/character/' + characterId + '/modify-attributes';
            const modifyAttributeBody = {
                attributeId: attributeId,
                skillPoints: addedValue
            }

            await requestWithAuthKey(modifyAttributeUrl, authKey, "PATCH", modifyAttributeBody);

            if (refreshFunc) {
                refreshFunc();
            }
        } catch (error) {
            console.log('modify attributes request error: ' + error);
        }

    }


    const attributeElements = attributesList
        .sort((a, b) => a.attribute.id - b.attribute.id)
        .map((attribute: Attribute) => {
            return (
                <View className={'w-full flex-row my-2'} key={attribute.attribute.id}>
                    <View className={classNames(
                        'mr-2',
                        GMMode || freeSkillPoints <= 0 ? 'w-full' : 'w-5/6',
                    )}>
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
                    {
                        !GMMode && freeSkillPoints > 0 &&
                        <TouchableOpacity
                            className={'bg-color-accent rounded-2xl p-2 w-12 justify-center '}
                            onPress={() => {
                                sendAttributeLvlUpRequest(
                                    Number(attribute.attribute.id),
                                    1
                                )
                                    .catch(error => console.log(error));

                                refreshFunc();
                            }}

                        >
                            <Text className={'text-xl text-color-white font-bold text-center'}>
                                +
                            </Text>
                        </TouchableOpacity>
                    }


                </View>
            );
        })


    return (
        <Section colorVariant={'light'} title={'Attributes'}>
            {
                freeSkillPoints > 0 &&
                <Text className={'text-base text-center text-color-white'}>
                    ({freeSkillPoints} free skill points)
                </Text>
            }
            <View className={''}>
                {attributeElements}
            </View>
        </Section>
    );
}




