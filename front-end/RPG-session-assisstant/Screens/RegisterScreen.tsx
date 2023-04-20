import React, {useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {isArrayFilledWithTrue, modifyElementInArrayByIndex} from "../utils/utils";
import axios from "axios";
import {Warning} from "../Components/Warning";
import AsyncStorage from "@react-native-async-storage/async-storage";

//import * as dotenv from 'dotenv' // see https://github.com/motdotla/dotenv#how-do-i-use-dotenv-with-import

export function RegisterScreen({navigation}: { navigation: any }) {
    const registerApiUrl =  "http://10.0.2.2:8080/api" +"/registration";

    const [mail, setMail] = useState('');
    const [password, setPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(3));

    const [warningValue, setWarningValue] = useState('');
    const [serverError, setServerError] = useState(null);


    function register() {
        axios.post(registerApiUrl, {
            login: mail,
            password: password
        })
            .then(function (response) {
                console.log(response);
                setServerError(null);
                //AsyncStorage.setItem('token', response.data.token);
                navigation.navigate('login');
            })
            .catch(function (error) {
                setServerError(error.message);
            });
    }

    //console.log(registerApiUrl)
    return (
        <View className={"flex-col justify-center h-full w-full"}>
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
                        regex={new RegExp("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")}//password validation regex
                        password
                    />

                    <CustomInput
                        placeholder={"repeat password"}
                        func={(value: string, isValid: boolean) => {
                            setRepeatedPassword(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 2, isValid));

                        }}
                        regex={new RegExp('^' + password + '$')}//validation regex: repeated password == password
                        password
                    />

                    <Btn
                        text={"Create account"}
                        disabled={!isArrayFilledWithTrue(areInputsValid)}
                        func={() => {
                            register();
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
