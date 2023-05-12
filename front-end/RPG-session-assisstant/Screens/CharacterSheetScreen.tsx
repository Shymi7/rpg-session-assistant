import React, {useEffect} from "react";
import {ScrollView, Text, TouchableOpacity, View} from "react-native";
import {Section} from "../Components/Section";
import {GETRequestWithAuthKey, getUserDataFromLocalStorage} from "../utils/utils";
import {API_URL} from "../env";
import {attribute, InventoryItem} from "../Components/InventoryItem";
import classNames from "classnames";

export function CharacterSheetScreen({route, navigation}: any) {

    const [characterData, setCharacterData] = React.useState<any>();

    async function getData() {
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            const getCharacterDataUrl = API_URL + '/api/room/' + route.params.roomId + '/character';
            const res = await GETRequestWithAuthKey(getCharacterDataUrl, authKey)

            setCharacterData(res.data);

        } catch (error) {
            console.log('get character data request error: ' + error);
        }
    }

    useEffect(() => {
        getData()
            .catch(error => console.error(error));
    }, [])

    function mainInfoSection() {



        return (
            <Section colorVariant={'dark'} title={characterData.name}>
                <Text className={'text-lg text-color-white'}>
                    Description
                </Text>
                {/*<Section colorVariant={'light'} hasPadding={false}>*/}
                {/*    <View className={classNames(*/}
                {/*        'bg-color-accent rounded-2xl',*/}
                {/*        'w-'+characterData.experience+'/12'*/}
                {/*    )}>*/}

                {/*    </View>*/}
                {/*</Section>*/}
            </Section>
        );
    }

    function attributesSection() {
        const attributeElements = characterData.characterAttributes.map((attribute: any, index: number) => {
            return (
                <View className={'w-full flex-row my-2'}>
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

    function questsSection() {
        function questElement(value: string, hasSeparator: boolean = true) {
            return (
                <View className={'w-full'}>
                    {
                        hasSeparator &&
                        <View className={'w-full bg-color-accent rounded-2xl'}></View>
                    }
                    <Text className={'text-color-white text-lg'}>
                        {value}
                    </Text>
                </View>
            );
        }

        console.log(characterData);

        return (
            <Section colorVariant={'dark'} title={'Quests'}>

                <View className={''}>
                    {/*{questElements}*/}
                </View>
            </Section>
        );
    }

    function inventorySection() {
        const inventoryItemElements = characterData.items.map((item: any, index: number) => {

            //create list of attributes of this item
            const attributes = item.itemAttributes.map((itemAttribute: any): attribute => {
                return {
                    name: itemAttribute.attribute.name,
                    value: itemAttribute.attributeValue,
                };
            })

            return (
                <InventoryItem name={item.name} attributes={attributes} description={item.description}/>
            );
        })
        return (
            <Section colorVariant={'light'} title={'Inventory'}>
                <View className={''}>
                    {inventoryItemElements}
                </View>

            </Section>
        );
    }

    return (
        <ScrollView>
            {characterData !== undefined &&
                <>
                    {mainInfoSection()}
                    {attributesSection()}
                    {questsSection()}
                    {inventorySection()}
                </>
            }


        </ScrollView>
    );
}
