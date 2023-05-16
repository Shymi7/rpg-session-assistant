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
]


export function Btn({text, func, disabled = false, iconIndex, additionalTailwindClasses}: Props) {

    //const iconPath = iconPaths[iconIndex];

    return(
        <TouchableOpacity
            onPress={func}
            className={classNames(
                "rounded-xl w-2/3 flex-row justify-center py-2 m-2",
                disabled ? "bg-color-accentInactive" : "bg-color-accent",
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
            {/*{*/}
            {/*    iconIndex &&*/}
            {/*    <Image*/}
            {/*        source={iconPaths[iconIndex]}*/}
            {/*        className={'w-10 h-12'}*/}
            {/*    />*/}
            {/*}*/}

        </TouchableOpacity>
    )
}
