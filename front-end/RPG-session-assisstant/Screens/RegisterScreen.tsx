import React, {useState} from "react";
import {View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {isArrayFilledWithTrue, modifyElementInArrayByIndex} from "../utils/utils";
import axios from "axios";
import {Warning} from "../Components/Warning";
import {API_URL} from "../env";

export function RegisterScreen({navigation}: { navigation: any }) {
    const registerApiUrl = API_URL + "/api/registration";

    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(3));

    const [serverError, setServerError] = useState(null);


    function register() {
        axios.post(registerApiUrl, {
            login: login,
            password: password
        })
            .then(function (response) {
                console.log(response);
                setServerError(null);
                navigation.navigate('login');
            })
            .catch(function (error) {
                setServerError(error.message);
            });
    }


    return (
        <View className={"flex-col justify-center h-full w-full"}>
            <Section variant={"light"}>
                <View className={'items-center px-4'}>
                    <CustomInput
                        placeholder={"login"}
                        func={(value: string, isValid: boolean) => {
                            setLogin(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 0, isValid));

                        }}
                        regex={/^[a-zA-Z0-9]{4,19}$/} //login validation regex:  4-19 letters or numbers
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
