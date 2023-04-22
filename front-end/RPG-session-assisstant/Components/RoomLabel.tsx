import {Button, Text, TouchableOpacity, View} from "react-native";
import {Section} from "./Section";

interface Props {
    roomName: string;
    characterData: string;
    roomId: number;
}

export function RoomLabel({roomName, characterData, roomId}: Props) {
    return(
        <Section variant={'dark'}>
            <View className={'flex-row justify-around'}>
                <View className={'flex-col w-3/4 justify-around'}>
                    
                </View>
                <TouchableOpacity>

                </TouchableOpacity>
            </View>
        </Section>
    )
}
