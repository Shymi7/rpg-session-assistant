import React, {useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";

export function SignInScreen({navigation}: { navigation: any }) {

    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState('');

    return (
        <View className={"flex-col justify-center h-full"}>
            <Section variant={"dark"}>
                <View className={'items-center px-4'}>
                    <CustomInput
                        placeholder={"mail"}
                        func={(value: string) => {
                            setMail(value)
                        }}
                        regex={/^\S+@\S+\.\S+$/} //simple mail validation regex
                    />

                    <CustomInput
                        placeholder={"password"}
                        func={(value: string) => {
                            setPassword(value)
                        }}
                        regex={/\b\w{4,15}\b/}//simple validation regex: min 4 chars, max 15
                        password
                    />

                    <CustomInput
                        placeholder={"repeat password"}
                        func={(value: string) => {
                            setRepeatedPassword(value)
                        }}
                        regex={/\b\w{4,15}\b/}//simple validation regex: min 4 chars, max 15
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
