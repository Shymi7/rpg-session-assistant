import React, {useState} from "react";
import {Text, View} from "react-native";
import {CustomInput} from "../Components/CustomInput";
import {getUserDataFromLocalStorage} from "../utils/utils";
import {API_URL, DEFAULT_CHARACTER_ATTRIBUTES} from "../env";
import axios from "axios";
import {Section} from "../Components/Section";
import {Btn} from "../Components/Btn";
import {Warning} from "../Components/Warning";

export function EnterNewRoomScreen({navigation}: { navigation: any }) {

    const [characterName, setCharacterName] = useState<string>('');
    const [roomName, setRoomName] = useState<string>('');
    const [password, setPassword] = useState<string>('');

    const [isInputValid, setIsInputValid] = useState<boolean>(true);

    const [serverErrorValue, setServerErrorValue] = useState<string>('');

    async function loginToNewRoom() {
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage();
            if (!authKey)
                throw new Error("Async storage error")

            //send request to create new character and then save id of the character
            const characterId = await createNewCharacter(authKey);

            //send request to actually enter new room
            await enterNewRoom(authKey, characterId);
            navigation.navigate('browseRooms');
        } catch (error) {
            console.log('Login error: ' + error);
            setServerErrorValue('Login to new room failed');
        }
    }

    async function createNewCharacter(authKey: string) {
        const url = API_URL + '/api/character/create';
        const response = await axios.post(url, {
            name: characterName,
            attributeNames: DEFAULT_CHARACTER_ATTRIBUTES
        }, {
            headers: {
                Authorization: authKey
            }
        });
        return response.data.id;
    }

    async function enterNewRoom(authKey: string, characterId: number) {
        const url = API_URL + '/api/room/enter-room';
        await axios.post(url, {
            roomName: roomName,
            password: password,
            characterId: characterId
        }, {
            headers: {
                Authorization: authKey
            }
        });
    }


    function enterRoomSection() {
        return (
                <Section colorVariant={"light"}>
                    <View className={'items-center px-4'}>
                        <Text className={"text-2xl font-bold text-center text-color-white"}>
                            Enter new room
                        </Text>
                        <CustomInput
                            placeholder={'Character name'}
                            func={(value: string, isValid: boolean) => {
                                setCharacterName(value);
                                setIsInputValid(isValid);
                            }}
                            regex={/^[\w\s-]{3,19}$/} //login validation regex:  4-19 letters or numbers
                        />

                        <CustomInput
                            placeholder={'Room name'}
                            func={(value: string, isValid: boolean) => {
                                setRoomName(value);
                            }}
                        />
                        <CustomInput
                            placeholder={'Password'}
                            password
                            func={(value: string, isValid: boolean) => {
                                setPassword(value);
                            }}
                        />

                        <Btn
                            text={"Enter"}
                            disabled={!(isInputValid && password !== '' && characterName !== '')}
                            func={() => {
                                loginToNewRoom();
                            }}
                        />

                    </View>
                </Section>
        )
    }


    return (
        <View className={'h-full justify-center'}>
            {enterRoomSection()}
            {
                (serverErrorValue !== "") &&
                <Warning text={serverErrorValue}/>
            }
        </View>
    );
}
