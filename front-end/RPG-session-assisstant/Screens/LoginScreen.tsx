import React, {useEffect, useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {modifyElementInArrayByIndex} from "../utils/utils";
import axios from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";

export function LoginScreen({navigation}: { navigation: any }) {

    // const registerApiUrl =  "//10.0.2.16:8080/api" +"/register";
    // const registerApiUrl =  "//10.0.2.16:8080/api" +"/register";
    const loginApiUrl =  "http://10.0.2.2:8080/api" +"/login";


    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(2));

    function logIn() {
        axios.post(loginApiUrl, {
            login: login,
            password: password
        }).then(res => {
            console.log(res);
            //AsyncStorage.setItem("token", res.data.token);

        }).catch(err => {
            console.log(err);
        })
    }

    return (
        <View className={"flex-col justify-center h-full"}>
            <Section variant={"light"}>
                <View className={'items-center px-4'}>
                    <CustomInput
                        placeholder={"enter login"}
                        func={(value: string, isValid: boolean) => {
                            setLogin(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 0, isValid));
                        }}
                    />

                    <CustomInput
                        placeholder={"enter password"}
                        func={(value: string, isValid: boolean) => {
                            setPassword(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 1, isValid));

                        }}
                        password
                    />
                    <Btn
                        text={"Log in"}
                        func={() => {
                            logIn();
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
