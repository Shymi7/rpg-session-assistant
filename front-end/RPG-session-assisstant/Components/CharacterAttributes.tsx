import {Section} from "./Section";
import {Text, TouchableOpacity, View} from "react-native";
import React from "react";
import {getUserDataFromLocalStorage, requestWithAuthKey} from "../utils/utils";
import {API_URL} from "../env";

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

    async function sendAttributeLvlUpRequest(attributeId: number,
                                             currentAttributeValue: number,
                                             addedValue = 1) {
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            //create new quest
            const modifyAttributeUrl = API_URL + '/api/character/' + characterId + '/modify-attributes';
            const modifyAttributeBody = {
                attributesLevelSet: [
                    {
                        attributeId: attributeId,
                        newLevel: currentAttributeValue + addedValue
                    }
                ]
            }
            const res = await requestWithAuthKey(modifyAttributeUrl, authKey, "PATCH", modifyAttributeBody);

            if (refreshFunc) {
                refreshFunc();
            }
        } catch (error) {
            console.log('modify attributes request error: ' + error);
        }

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
                        console.log(Number(attribute.attribute.id),Number(attribute.attributeLevel))
                        // sendAttributeLvlUpRequest(
                        //     Number(attribute.attribute.id),
                        //     Number(attribute.attributeLevel)
                        // )
                        //     .catch(error => console.log(error));
                        refreshFunc();
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




