import {View} from "react-native";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {RoomLabel} from "../Components/RoomLabel";
import {API_URL} from "../env";
import {getUserDataFromLocalStorage} from "../utils/utils";
import {Btn} from "../Components/Btn";
import {useFocusEffect} from "@react-navigation/native";


export function BrowseRoomsScreen({navigation}: { navigation: any }) {

    const [roomLabelElements, setRoomLabelElements] = useState<JSX.Element[]>([]);


    //refresh rooms list after entering new room
    useFocusEffect(
        React.useCallback(() => {
            handleDataFromApi();
        }, [])
    );


    async function getRoomsData(url: string, key: string | null) {
        return await axios.get(url, {
            headers: {
                Authorization: key,
            }
        })
    }

    function roomLabelFromData(room: any, isGM: boolean = false): JSX.Element {
        return <RoomLabel
            roomName={room.name}
            roomId={room.id}
            isGM={isGM}
            navigation={navigation}
            key={room.id}
        />
    }

    function handleDataFromApi() {
        //load all rooms where player is a character or a game master
        getUserDataFromLocalStorage()
            .then(({authKey, playerId}) => {
                const playerInRoomsUrl = API_URL + '/api/player/' + playerId + '/player-in-rooms';
                const GMInRoomsUrl = API_URL + '/api/player/' + playerId + '/gamemaster-in-rooms';

                let tempRoomLabelElements: JSX.Element[] = [];

                const promises = [
                    // rooms where player has a character
                    getRoomsData(playerInRoomsUrl, authKey)
                        .then(res => {
                            const elements = res.data.map((room: any) => {
                                return roomLabelFromData(room);
                            });

                            tempRoomLabelElements = tempRoomLabelElements.concat(elements);
                        }),

                    //rooms where player is a game master
                    getRoomsData(GMInRoomsUrl, authKey)
                        .then(res => {
                            const elements = res.data.map((room: any) => {
                                return roomLabelFromData(room, true);
                            });

                            tempRoomLabelElements = tempRoomLabelElements.concat(elements);
                        })
                ];

                return Promise.all(promises)
                    .then(() => tempRoomLabelElements);
            })
            .then(tempRoomLabelElements => {
                setRoomLabelElements(tempRoomLabelElements);
            })
    }


    useEffect(() => {
        handleDataFromApi();
    }, []);


    return (
        <View className={'items-center'}>

            {roomLabelElements}
            <Btn
                func={() => {
                    navigation.navigate('enterNewRoom');
                }}
                text={'Enter new room'}
            />


            <Btn
                func={() => {
                    navigation.navigate('createNewRoom');
                }}
                text={'Create new room'}
            />
        </View>
    )
}
