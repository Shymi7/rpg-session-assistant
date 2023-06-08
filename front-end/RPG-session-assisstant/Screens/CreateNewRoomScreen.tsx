import React, {useState} from "react";
import {Text, View} from "react-native";
import {Section} from "../Components/Section";
import {CustomInput} from "../Components/CustomInput";
import {
    getUserDataFromLocalStorage,
    isArrayFilledWithTrue,
    modifyElementInArrayByIndex,
    requestWithAuthKey
} from "../utils/utils";
import {Btn} from "../Components/Btn";
import {Warning} from "../Components/Warning";
import {API_URL} from "../env";

export function CreateNewRoomScreen({navigation}: { navigation: any }) {

    const [roomNameInputValue, setRoomNameInputValue] = useState('');
    const [passwordInputValue, setPasswordInputValue] = useState('');
    const [repeatedPasswordInputValue, setRepeatedPasswordInputValue] = useState('');
    const [roomCapacityInputValue, setRoomCapacityInputValue] = useState('');

    const [areInputsValid, setAreInputsValid] = useState<boolean[]>(Array(4));

    const [serverErrorValue, setServerErrorValue] = useState(null);

    async function sendCreateRoomRequest(){
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            const createNewRoomUrl = API_URL + '/api/room/create-room';
            const createNewRoomBody = {
                capacity: parseInt(roomCapacityInputValue),
                password: passwordInputValue,
                name: roomNameInputValue
            };

            await requestWithAuthKey(createNewRoomUrl, authKey, "POST", createNewRoomBody);

            // if (refreshFunc) {
            //     refreshFunc();
            // }
        } catch (error) {
            console.log('create new room request error: ' + error);
        }
    }


    return (
        <View className={"flex-col justify-center h-full w-full"}>
            <Section colorVariant={"light"} title={'Create new room'}>
                <View className={'items-center px-4'}>
                    <CustomInput
                        placeholder={"Room name"}
                        func={(value: string, isValid: boolean) => {
                            setRoomNameInputValue(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 0, isValid));

                        }}
                        regex={/^[a-zA-Z0-9]{4,19}$/} //login validation regex:  4-19 letters or numbers
                    />
                    <CustomInput
                        placeholder={"Room capacity"}
                        func={(value: string, isValid: boolean) => {
                            setRoomCapacityInputValue(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 1, isValid));

                        }}
                        regex={/^([1-9]|[1-9][0-9]|100)$/} //validation regex:  number from 1 to 100
                    />

                    <CustomInput
                        placeholder={"Password"}
                        func={(value: string, isValid: boolean) => {
                            setPasswordInputValue(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 2, isValid));

                        }}
                        regex={new RegExp("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")}//password validation regex
                        password
                    />

                    <CustomInput
                        placeholder={"Repeat password"}
                        func={(value: string, isValid: boolean) => {
                            setRepeatedPasswordInputValue(value);
                            setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 3, isValid));

                        }}
                        regex={new RegExp('^' + passwordInputValue + '$')}//validation regex: repeated password == password
                        password
                    />

                    <Btn
                        text={"Create account"}
                        disabled={!isArrayFilledWithTrue(areInputsValid)}
                        func={async () => {
                            await sendCreateRoomRequest();
                            navigation.navigate('browseRooms');
                        }}
                    />

                </View>


            </Section>

            {

                (serverErrorValue !== null) &&
                <Warning text={serverErrorValue}/>
            }

        </View>
    );
}
