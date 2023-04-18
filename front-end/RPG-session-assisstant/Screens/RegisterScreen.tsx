import React, {useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {isArrayFilledWithTrue, modifyElementInArrayByIndex} from "../utils/utils";
import axios from "axios";
import {Warning} from "../Components/Warning";

//import * as dotenv from 'dotenv' // see https://github.com/motdotla/dotenv#how-do-i-use-dotenv-with-import

export function RegisterScreen({navigation}: { navigation: any }) {
    //dotenv.config()
    const registerApiUrl =  "//10.0.2.2:8080/api" +"/register";
    // const registerApiUrl =  "//192.168.56.1:8080/api" +"/register";

    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(3));

    const [warningValue, setWarningValue] = useState('');
    const [serverError, setServerError] = useState(null);


    //

    console.log(registerApiUrl)
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
                        regex={new RegExp('^' + password + '$')}//validation regex: repeated password = password
                        password
                    />

                    <Btn
                        text={"Create account"}
                        disabled={!isArrayFilledWithTrue(areInputsValid)}
                        func={() => {
                            axios.post(registerApiUrl, {
                                login: mail,
                                password: password
                            })
                                .then(function (response) {
                                    console.log(response);
                                })
                                .catch(function (error) {
                                    console.log(error);
                                    setServerError(error);
                                });
                            console.log("sdfasdf");
                        }}
                    />

                </View>


            </Section>

            {

                (serverError !== null) &&
                <Warning text={serverError}/>
            }

        </View>

    );
}
