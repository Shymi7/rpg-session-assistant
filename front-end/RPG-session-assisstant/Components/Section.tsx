import {Text, View} from "react-native";
import {PropsWithChildren} from "react";
import classNames from "classnames";

type Props = {
    colorVariant: "light" | "dark";
    title?: string;
    hasPadding? : boolean;
    hasMargin? : boolean;
}

export function Section({children, colorVariant, title, hasPadding = true, hasMargin = true}: PropsWithChildren<Props>) {
    return (
        <View
            className={classNames(
                "rounded-2xl h-auto text-color-white",
                colorVariant == "light" ? "bg-color-greyLight" : "bg-color-greyDark",
                hasPadding ? 'p-4' : '',
                hasMargin ? 'm-4' : '',
            )}
            style={{
                shadowColor: "#000000",
                shadowOffset: {
                    width: 0,
                    height: 14,
                },
                shadowOpacity: 0.24,
                shadowRadius: 15.38,
                elevation: 19
            }}
        >
            {
                title &&
                <Text
                    className={'font-bold text-3xl text-center text-color-white'}
                >
                    {title}
                </Text>
            }
            {children}
        </View>
    );
}
