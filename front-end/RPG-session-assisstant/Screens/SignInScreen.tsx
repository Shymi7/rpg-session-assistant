import React, {useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {modifyElementInArrayByIndex} from "../utils/utils";

export function SignInScreen({navigation}: { navigation: any }) {

    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(3));

    console.log(areInputsValid);
    return (
        <View className={"flex-col justify-center h-full"}>
            <Section variant={"light"}>
                <View className={'items-center px-4'}>
                    <CustomInput
                        placeholder={"mail"}
                        func={(value: string, isValid: boolean) => {
                            setMail(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 0, isValid));

                        }}
                        regex={/^\S+@\S+\.\S+$/} //simple mail validation regex
                    />

                    <CustomInput
                        placeholder={"password"}
                        func={(value: string, isValid: boolean) => {
                            setPassword(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 1, isValid));

                        }}
                        regex={/\b\w{4,15}\b/}//simple validation regex: min 4 chars, max 15
                        password
                    />

                    <CustomInput
                        placeholder={"repeat password"}
                        func={(value: string, isValid: boolean) => {
                            setRepeatedPassword(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 2, isValid));

                        }}
                        regex={new RegExp('^'+password+'$')}//validation regex: repeated password = password
                        password
                    />

                    <Btn
                        text={"Create account"}
                        func={() => {

                        }}
                    />

                </View>


            </Section>
        </View>

    );
}
