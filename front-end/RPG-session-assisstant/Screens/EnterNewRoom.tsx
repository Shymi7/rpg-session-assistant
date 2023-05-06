import React, {useState} from "react";
import {Text, TouchableOpacity, View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {getUserDataFromLocalStorage} from "../utils/utils";
import {API_URL} from "../env";
import axios from "axios";
import {Section} from "../Components/Section";

export function EnterNewRoom() {

    const [roomName, setRoomName] = useState<string>('');
    const [password, setPassword] = useState<string>('');



    function loginToNewRoom(){
        getUserDataFromLocalStorage()
            .then(({authKey, playerId}) => {
                const url = API_URL + '/api/room/enter-room';
                axios.post(url, {
                    roomName: roomName,
                    password: password,
                    characterId: parseInt(playerId!)
                }, {
                    headers: {
                        Authorization: authKey
                    }
                }).then((res) => {
                    console.log(res);
                }).catch(err => {
                    console.log(err);
                });
            });
    }


    function enterRoomSection() {
        return (
            <Section variant={"light"}>
                <Text className={"text-2xl font-bold text-center text-color-white"}>
                    Enter new room
                </Text>
                <View className={'flex-row w-full'}>
                    <View className={"flex-col w-3/4"}>
                        <CustomInput
                            placeholder={'Room name'}
                            func={(value: string, isValid: boolean) => {
                                setRoomName(value);
                            }
                            }/>
                        <CustomInput
                            placeholder={'Password'}
                            password
                            func={(value: string, isValid: boolean) => {
                                setPassword(value);
                            }}
                        />
                    </View>
                    <TouchableOpacity
                        className={'bg-color-accent rounded-xl w-1/6 justify-center items-center p-2 mx-5 my-2'}
                        onPress={() => {
                            loginToNewRoom();
                        }}
                    >
                        <Text className={'text-4xl text-color-white'}>
                            +
                        </Text>

                    </TouchableOpacity>
                </View>

            </Section>
        )
    }


    return (
        <View>
            <Text>
                NewCharacterScreen
            </Text>
        </View>
    );
}
