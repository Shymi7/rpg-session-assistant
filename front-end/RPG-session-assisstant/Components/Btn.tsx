import {Button, Text, TouchableOpacity} from "react-native";
import classNames from "classnames";

interface Props {
    text: string;
    func: () => void;
    disabled?: boolean;
}

export function Btn({text, func, disabled = false}: Props) {
    return(
        <TouchableOpacity
            className={classNames(
                "rounded-xl w-2/3 flex-row justify-center py-2 m-2",
                disabled ? "bg-color-accentInactive" : "bg-color-accent"
            )}
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
            disabled={disabled}
        >
            <Text
                className={"text-color-white text-xl font-bold"}
            >
                {text}
            </Text>
        </TouchableOpacity>
    )
}
