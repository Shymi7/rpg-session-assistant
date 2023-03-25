import {TextInput} from "react-native";
import React, {useState} from "react";
import classNames from "classnames";


interface Props {
    placeholder: string;
    func: (value: string, isValid: boolean) => void;
    password?: boolean;
    regex?: RegExp;
    variant?: "light" | "dark";
}

export function CustomInput({
                                placeholder,
                                func, //function that is called on change
                                password = false,
                                regex = /[\\s\\S]*/,
                                variant = "light"
                            }: Props) {

    const [isValid, setIsValid] = useState(true)

    function handleInputChange(textValue: string): void {
        if (textValue.match(regex)) {
            setIsValid(true);
            func(textValue, true);
            return;
        }
        setIsValid(false);
        func(textValue, false);
    }

    const tailwindColor = require("../tailwind.config");
    return (


        <TextInput
            className={classNames(
                "rounded-xl m-2 px-3 py-2 text-color-greyLight font-bold text-xl w-full",
                isValid ? "bg-color-white" : "bg-color-warning"
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
            placeholder={placeholder}
            placeholderTextColor={tailwindColor.theme.extend.colors.color["greyLight"]}
            onChangeText={text => handleInputChange(text)}
            secureTextEntry={password}
        />


    );
}
