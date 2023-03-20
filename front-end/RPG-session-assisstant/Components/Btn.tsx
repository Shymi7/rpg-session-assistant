import {Button, Text, TouchableOpacity} from "react-native";

interface Props {
    text: string;
    func: () => void;
}

export function Btn({text, func}: Props) {
    return(
        <TouchableOpacity
            className={"bg-color-accent rounded-xl w-2/3 flex-row justify-center py-2 m-2"}
            onPress={func}
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
        </TouchableOpacity>
    )
}
