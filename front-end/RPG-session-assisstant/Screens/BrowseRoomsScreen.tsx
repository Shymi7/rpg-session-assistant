import {Button, Text, TouchableOpacity, View} from "react-native";
import {useEffect, useState} from "react";
import axios from "axios";
import {RoomLabel} from "../Components/RoomLabel";
import {API_URL} from "../env";
import AsyncStorage from "@react-native-async-storage/async-storage";
import {Section} from "../Components/Section";
import {CustomInput} from "../Components/CustomInput";


export function BrowseRoomsScreen({navigation}: { navigation: any }) {

    let roomsData: any;

    const [roomLabelElements, setRoomLabelElements] = useState<JSX.Element[]>([]);

    const [roomName, setRoomName] = useState<string>('');
    const [password, setPassword] = useState<string>('');


    let authKey: string | null;
    let playerId: string | null;


    async function getDataFromLocalStorage() {
        authKey = await AsyncStorage.getItem('@loginAuthKey');
        playerId = await AsyncStorage.getItem('@loginUserId');
    }

    async function getRoomsData(url: string) {
        return await axios.get(url, {
            headers: {
                Authorization: authKey,
            }
        })
    }

    function roomLabelFromData(room: any, index: number): JSX.Element {
        return <RoomLabel
            roomName={room.name}
            characterData={room.characterData}
            roomId={room.id}
            index={index}
        />
    }

    useEffect(() => {

        //load all rooms where player is a character or a game master
        getDataFromLocalStorage()
            .then(() => {
                const playerInRoomsUrl = API_URL + '/api/player/' + playerId + '/player-in-rooms';
                const GMInRoomsUrl = API_URL + '/api/player/' + playerId + '/gamemaster-in-rooms';

                let tempRoomLabelElements: JSX.Element[] = [];

                const promises = [
                    // rooms where player has a character
                    getRoomsData(playerInRoomsUrl)
                        .then(res => {
                            const elements = res.data.map((room: any, index: number) => {
                                roomLabelFromData(room, index);
                            });

                            tempRoomLabelElements.concat(elements);
                        }),

                    //rooms where player is a game master
                    getRoomsData(GMInRoomsUrl)
                        .then(res => {
                            const elements = res.data.map((room: any, index: number) =>
                                roomLabelFromData(room, index)
                            );

                            tempRoomLabelElements = tempRoomLabelElements.concat(elements);
                        })
                ];

                return Promise.all(promises)
                    .then(() => tempRoomLabelElements);
            })
            .then(tempRoomLabelElements => {
                setRoomLabelElements(tempRoomLabelElements);
            })


    }, []);

    function enterRoomSection() {
        return (
            <Section variant={"light"}>
                <Text className={"text-2xl font-bold text-center text-color-white" }>
                    Enter new room
                </Text>
                <View className={'flex-row w-full'}>
                    <View className={"flex-col w-3/4"}>
                        <CustomInput
                            placeholder={'Room name'}
                            func={(value: string, isValid: boolean) => {
                                setRoomName(value);
                                //setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 0, isValid));
                            }
                            }/>
                        <CustomInput placeholder={'Password'} func={(value: string, isValid: boolean) => {
                            setPassword(value);
                            //setAreInputsValid(modifyElementInArrayByIndex(areInputsValid, 0, isValid));
                        }}/>
                    </View>
                    <TouchableOpacity
                        className={'bg-color-accent rounded-xl w-1/6 justify-center items-center p-2 mx-5 my-2'}
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
                BrowseRoomsScreen
            </Text>
            {roomLabelElements}
            {enterRoomSection()}
        </View>
    )
}
