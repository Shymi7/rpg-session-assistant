import {Button, Text, TouchableOpacity, View} from "react-native";

interface Props {
    text: string;
}

export function Warning({text}: Props) {
    return(
        <View
            className={"bg-color-warning rounded-xl w-full flex-row justify-center py-2 m-2"}

            style={{
                shadowColor: "#000000",
                shadowOffset: {
                    width: 0,
                    height: 14,
                },
                shadowOpacity:  0.24,
                shadowRadius: 15.38,
                elevation: 19
            }}
        >
            <Text
                className={"text-color-white text-xl font-bold"}
            >
                {text}
            </Text>
        </View>
    )
}
