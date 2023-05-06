import {View} from "react-native";
import {PropsWithChildren} from "react";
import classNames from "classnames";

type Props = {
    variant: "light" | "dark";
}

export function Section({children, variant}: PropsWithChildren<Props>) {
    return (
        <View
            className={classNames(
                "rounded-2xl m-4 p-2 h-auto ",
                variant == "light" ? "bg-color-greyLight" : "bg-color-greyDark"
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
        >
            {children}
        </View>
    );
}
