import React, {useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {modifyElementInArrayByIndex} from "../utils/utils";

export function LoginScreen({navigation}: { navigation: any }) {

    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(2));

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
                    <Btn
                        text={"Log in"}
                        func={() => {
                            navigation.navigate('characterSheet');
                        }}
                    />
                    <Btn
                        text={"Sign in"}
                        func={() => {
                            navigation.navigate('signIn');
                        }}
                    />
                </View>


            </Section>
        </View>

    );
}
