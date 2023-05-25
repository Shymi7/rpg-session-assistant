import {Button, Image, Text, TouchableOpacity} from "react-native";
import classNames from "classnames";

interface Props {
    func: () => void;
    text?: string;
    disabled?: boolean;
    iconIndex?: number;
    additionalTailwindClasses?: string;
}

//necessary, require function can't use dynamic values
const iconPaths = [
    require('../Icons/details.png'),
    require('../Icons/enterRoom.png'),
    require('../Icons/openList.png'),
    require('../Icons/checked.png'),
]


export function Btn({text, func, disabled = false, iconIndex, additionalTailwindClasses}: Props) {
    return(
        <TouchableOpacity
            onPress={func}
            className={classNames(
                "rounded-xl flex-row justify-center py-2 m-2",
                disabled ? "bg-color-accentInactive" : "bg-color-accent",
                text && 'w-1/2',
                additionalTailwindClasses && additionalTailwindClasses
            )}
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
            {
                text &&
                <Text className={"text-color-white text-xl font-bold"}>
                    {text}
                </Text>
            }
            {
                iconIndex !== undefined &&
                <Image
                    source={iconPaths[iconIndex]}
                    className={'w-6 h-6'}
                />
            }

        </TouchableOpacity>
    )
}
