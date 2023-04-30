import {Button, Text, TouchableOpacity, View} from "react-native";
import {Section} from "./Section";

interface Props {
    roomName: string;
    characterData: string;
    roomId: number;
    index: number;
    isGM?: boolean;
}

export function RoomLabel({roomName, characterData, roomId, index, isGM=false}: Props) {
    return(
        <Section variant={'dark'} key={index}>
            <View className={'flex-row justify-around text-color-white h-20'}>
                <View className={'flex-col w-3/4 justify-around'}>
                    <Text className={'text-xl text-color-white'}>
                        {roomName}
                    </Text>
                    <Text className={'text-xl text-color-white'}>
                        {characterData}
                    </Text>
                </View>
                <TouchableOpacity>
                    <Text>
                        {roomId}
                    </Text>
                </TouchableOpacity>
            </View>
        </Section>
    )
}
