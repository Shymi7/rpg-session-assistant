import {TextInput} from "react-native";
import React, {useState} from "react";
import classNames from "classnames";


interface Props {
    placeholder: string;
    func?: (value: string, isValid: boolean) => void;
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
        if(func === undefined)
            return;

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
                "rounded-xl m-2 px-3 py-2 font-bold text-xl w-full text-color-accent",
                isValid ? "bg-color-white" : "bg-color-warning",
                //isValid ? "text-color-accent" : "text-color-greyLight" //TODO: for some reason this causes text value of input to disappear
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
            onChangeText={(text) => {
                if(func)
                    handleInputChange(text);
            }}
            secureTextEntry={password}
        />


    );
}
