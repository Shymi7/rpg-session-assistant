import {Section} from "./Section";
import {Text, TouchableOpacity, View} from "react-native";
import {useState} from "react";

export type attribute = {
    name: string;
    value: number;
}


type Props = {
    name: string;
    attributes: attribute[];
    description: string;

}

export function InventoryItem({name, attributes, description}: Props) {

    const [areAttributesVisible, setAreAttributesVisible] = useState(false);


    const attributesElements = attributes.map((itemAttribute: attribute) => {
        return (
            <View className={'flex-row justify-between w-1/2'} key={Math.random()}>
                <Text className={'text-color-accent'}>{itemAttribute.name}:</Text>
                <Text className={'text-color-white'}>{itemAttribute.value}</Text>
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
                        <Text
                            className={'text-xl text-color-greyDark font-bold text-center'}
                        >
                            {areAttributesVisible ? '^' : 'v'}
                        </Text>
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
