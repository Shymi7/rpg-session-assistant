import {Section} from "./Section";
import {Image, Text, TouchableOpacity, View} from "react-native";
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

interface Attribute {
    attribute: {
        id: number;
        name: string;
    };
    attributeValue: number;
}

interface Item {
    name: string;
    description: string;
    id: number;
    itemAttributes: Attribute[];
}

interface Props {
    itemsList: Item[];
    refreshFunc: () => void;
    GMMode?: boolean;
    characterId?: number;


}

function InventoryItem({name, itemAttributes, description}: Item) {

    const [areAttributesVisible, setAreAttributesVisible] = useState(false);


    const attributesElements = itemAttributes.map((itemAttribute: Attribute) => {
        return (
            <View className={'flex-row justify-between w-1/2'} key={Math.random()}>
                <Text className={'text-color-accent'}>{itemAttribute.attribute.name}:</Text>
                <Text className={'text-color-white'}>{itemAttribute.attributeValue}</Text>
            </View>
        );
    })

    return (
        <View className={'m-2'}>
            <Section colorVariant={'dark'} key={Math.random()} hasPadding={false} hasMargin={false}>
                <View className={'flex-row justify-between items-center p-2'}>
                    <Text className={'text-lg text-color-white'}>
                        {name}
                    </Text>
                    <TouchableOpacity
                        className={'bg-color-accent rounded-2xl p-2 w-10'}
                        onPress={() => {
                            setAreAttributesVisible(a => !a);
                        }}
                    >
                        <Image
                            source={require('../Icons/details.png')}
                            className={'w-6 h-6'}
                        />
                    </TouchableOpacity>
                </View>
                {
                    areAttributesVisible &&
                    <View className={'p-3'}>
                        {attributesElements}
                        <Text className={'text-color-white'}>
                            {description}
                        </Text>
                    </View>
                }
            </Section>
        </View>

    );
}



export function CharacterInventory({itemsList, refreshFunc, GMMode = false, characterId}: Props) {

    const [itemNameInputValue, setItemNameInputValue] = useState('');
    const [itemDescriptionInputValue, setItemDescriptionInputValue] = useState('');
    const [itemPriceInputValue, setItemPriceInputValue] = useState('');
    const [itemWeightInputValue, setItemWeightInputValue] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(4));

    async function sendCreateNewItemRequest(){
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            const createNewItemUrl = API_URL + '/api/item/create';
            const createNewItemBody = {
                name: itemNameInputValue,
                description: itemDescriptionInputValue,
                itemAttributes:[
                    {
                        attribute:{
                            name: "Price"
                        },
                        attributeValue: Number(itemPriceInputValue)
                    },
                    {
                        attribute:{
                            name: "Weight"
                        },
                        attributeValue: Number(itemWeightInputValue)
                    }
                ]
            };

            const res = await requestWithAuthKey(createNewItemUrl, authKey, "POST", createNewItemBody);
            const idOfNewItem = res.data.id;

            //assign that quest to character
            const addItemToCharacterUrl = API_URL + '/api/character/add-item';
            const addItemToCharacterBody = {
                characterId: characterId,
                entityId: idOfNewItem
            }
            await requestWithAuthKey(addItemToCharacterUrl, authKey, "PATCH", addItemToCharacterBody);

            if (refreshFunc) {
                refreshFunc();
            }
        } catch (error) {
            console.log('modify attributes request error: ' + error);
        }
    }



    const addNewItemsUiElements =
        <View className={'flex-col items-center'}>
            <CustomInput
                placeholder={'Item name'}
                func={(value: string, isValid: boolean) => {
                    setItemNameInputValue(value);
                    setAreInputsValid(prevState =>
                        modifyElementInArrayByIndex(prevState, 0, isValid)
                    )
                }}
                regex={/^.{3,30}$/}

            />
            <CustomInput
                placeholder={'Item description'}
                func={(value: string, isValid: boolean) => {
                    setItemDescriptionInputValue(value);
                    setAreInputsValid(prevState =>
                        modifyElementInArrayByIndex(prevState, 1, isValid)
                    )
                }}
                regex={/^.{3,200}$/}

            />
            <View className={'flex-row'}>

                <CustomInput
                    placeholder={'Price'}
                    additionalTailwindClasses={'mr-4'}
                    func={(value: string, isValid: boolean) => {
                        setItemPriceInputValue(value);
                        setAreInputsValid(prevState =>
                            modifyElementInArrayByIndex(prevState, 2, isValid)
                        )
                    }}
                    regex={/^\d+$/}
                />
                <CustomInput
                    placeholder={'Weight'}
                    func={(value: string, isValid: boolean) => {
                        setItemWeightInputValue(value);
                        setAreInputsValid(prevState =>
                            modifyElementInArrayByIndex(prevState, 3, isValid)
                        )
                    }}
                    regex={/^\d+$/}
                />
            </View>
            <Btn
                text={'Add item'}
                func={() => {
                    sendCreateNewItemRequest();
                }}
                disabled={!isArrayFilledWithTrue(areInputsValid)}
            />
        </View>


    const inventoryItemElements = itemsList
        .sort((a, b) => b.id - a.id)//DESC sort by item.id
        .map((item: Item) => {
            return (
                <InventoryItem
                    name={item.name}
                    description={item.description}
                    id={item.id}
                    itemAttributes={item.itemAttributes}
                    key={item.id}
                />
            );
        })


    return (
        <Section colorVariant={'light'} title={'Inventory'}>
            <View className={''}>
                {GMMode && addNewItemsUiElements}
                {inventoryItemElements}
            </View>
        </Section>
    );
}




