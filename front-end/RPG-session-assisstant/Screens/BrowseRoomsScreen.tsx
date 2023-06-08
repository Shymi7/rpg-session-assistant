import {ScrollView, View} from "react-native";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {RoomLabel} from "../Components/RoomLabel";
import {API_URL} from "../env";
import {GETRequestWithAuthKey, getUserDataFromLocalStorage} from "../utils/utils";
import {Btn} from "../Components/Btn";
import {useFocusEffect} from "@react-navigation/native";


export function BrowseRoomsScreen({navigation}: { navigation: any }) {

    const [roomLabelElements, setRoomLabelElements] = useState<JSX.Element[]>([]);


    //refresh rooms list after entering new room
    useFocusEffect(
        React.useCallback(() => {
            sendLoadRoomListRequest();
        }, [])
    );


    async function getRoomsData(url: string, key: string | null) {
        return await axios.get(url, {
            headers: {
                Authorization: key,
            }
        })
    }



    async function sendLoadRoomListRequest() {
        try {
            const playerData = await getUserDataFromLocalStorage();
            const authKey = playerData.authKey;
            const playerId = playerData.playerId;

            const getRoomsWhereIsPlayerUrl = API_URL + '/api/player/' + playerId + '/player-in-rooms';
            const getRoomsWherePlayerIsGm = API_URL + '/api/player/' + playerId + '/gamemaster-in-rooms';



            //send first request to load rooms where player has a character
            let res = await GETRequestWithAuthKey(getRoomsWhereIsPlayerUrl, authKey)
            let tempRoomLabelElements = res.data.map((room: any) => {
                return roomLabelFromData(room);
            });

            //send second request to load rooms where player is gamemaster
            res = await GETRequestWithAuthKey(getRoomsWherePlayerIsGm, authKey)
            const gmRoomElements = res.data.map((room: any) => {
                return roomLabelFromData(room, true);
            });
            tempRoomLabelElements = tempRoomLabelElements.concat(gmRoomElements);


            setRoomLabelElements(tempRoomLabelElements);
        } catch (error) {
            console.log('Load room list request error: ' + error);
        }
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
        sendLoadRoomListRequest();
    }, []);

    function roomLabelFromData(room: any, isGM: boolean = false): JSX.Element {
        return <RoomLabel
            roomName={room.name}
            roomId={room.id}
            isGM={isGM}
            navigation={navigation}
            key={room.id}
        />
    }

    return (
        <ScrollView>
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


        </ScrollView>
    )
}
