import React, {useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {modifyElementInArrayByIndex} from "../utils/utils";
import axios from "axios";
import AsyncStorage from "@react-native-async-storage/async-storage";
import {Warning} from "../Components/Warning";

export function LoginScreen({navigation}: { navigation: any }) {

    const loginApiUrl = "http://10.0.2.2:8080" + "/login";

    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(2));

    const [serverError, setServerError] = useState<string | null>(null);

    function logIn() {
        axios.post(loginApiUrl, {
            login: login,
            password: password
        }).then(res => {
            const authKey = res.headers.authorization;
            try {
                AsyncStorage.setItem('@storage_Key', authKey)
                    .then(() => {
                        setServerError(null);
                        navigation.navigate('characterSheet');
                    }).catch(err => {
                    console.log(err);
                });
            } catch (e) {
                console.log("async storage error: " + e);
            }
        }).catch(err => {
            setServerError("Login failed: \n" + err.message);
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
                            navigation.navigate('selectRoom');
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
