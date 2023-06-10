import React, {useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {modifyElementInArrayByIndex, saveToAsyncStorage} from "../utils/utils";
import axios from "axios";
import {Warning} from "../Components/Warning";
import {API_URL} from "../env";

export function LoginScreen({navigation}: { navigation: any }) {

    const loginApiUrl = API_URL + "/login";
    const getUserIdApiUrl = API_URL + "/api/player";

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

            const url = getUserIdApiUrl + "?login=" + login
            axios.get(url, {
                headers: {
                    Authorization: authKey
                }
            }).then(res => {
                const playerId = res.data.id.toString();

                saveToAsyncStorage([
                    {key: '@loginAuthKey', value: authKey},
                    {key: '@loginUserId', value: playerId}
                ]).then(() => {
                    setServerError(null);
                    navigation.navigate('browseRooms');
                }).catch(err => {
                    console.log("async storage error: " + err);
                });

            }).catch(err => {
                console.log("get player id error: " + err);
            })

        }).catch(err => {
            setServerError("Login failed: \n" + err.message);
            console.log("login error" + err);
        })
    }

    return (
        <View className={"flex-col justify-center h-full"}>
            <Section colorVariant={"light"}>
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
                        password={true}
                    />
                    <Btn
                        text={"Log in"}
                        func={() => {
                            logIn();
                        }}
                    />
                    <Btn
                        text={"Sign up"}
                        func={() => {
                            navigation.navigate('register');
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
