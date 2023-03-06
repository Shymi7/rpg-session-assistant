import React from "react";
import {Button, Text, TextInput, TouchableOpacity, View} from "react-native";

export function LoginScreen({navigation}) {
    const inputStyle =
        'bg-gray-300 border-2 border-default-800 rounded-xl my-6 px-3 py-2 text-default-800 font-bold text-xl w-full';
    const buttonStyle =
        'w-2/3 bg-default-800 rounded-xl m-4 justify-center items-center py-2';
    const buttonTextStyle =
        'font-bold text-xl text-white'

    const inputPlaceholderColor = '#166534';
    return (
        <View>
            <View className={'items-center px-4'}>
                <TextInput
                    className={inputStyle}
                    placeholderTextColor={inputPlaceholderColor}
                    placeholder={'Room name'}
                />
                <TextInput
                    className={inputStyle}
                    placeholderTextColor={inputPlaceholderColor}
                    placeholder={'Room password'}
                />
                <TextInput
                    className={inputStyle}
                    placeholderTextColor={inputPlaceholderColor}
                    placeholder={'Player password'}
                />
                <TouchableOpacity
                    className={buttonStyle}
                    onPress={() => navigation.navigate('characterSheet')}
                >
                    <Text className={buttonTextStyle}>
                        Enter room
                    </Text>
                </TouchableOpacity>

                <TouchableOpacity
                    className={buttonStyle}
                    onPress={() => navigation.navigate('newCharacter')}
                >
                    <Text className={buttonTextStyle}>
                        Create new room
                    </Text>
                </TouchableOpacity>
            </View>




        </View>
    );
}
