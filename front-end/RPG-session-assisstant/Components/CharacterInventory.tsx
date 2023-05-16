import {Section} from "./Section";
import {Image, Text, TouchableOpacity, View} from "react-native";
import React, {useState} from "react";

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


export function CharacterInventory({itemsList}: Props) {

    const inventoryItemElements = itemsList.map((item: Item) => {
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
                {inventoryItemElements}
            </View>
        </Section>
    );
}




