import {Text, View} from "react-native";
import {useEffect} from "react";
import axios from "axios";
import {RoomLabel} from "../Components/RoomLabel";
import {API_URL} from "../env";
import AsyncStorage from "@react-native-async-storage/async-storage";


export function BrowseRoomsScreen({navigation}: { navigation: any }) {

    let authKey: string;
    let roomsData: any;
    let roomLabelElements: JSX.Element[] = [];

    useEffect(() => {

        console.log("asdfasdf")
        console.log(AsyncStorage.getItem('@loginAuthKey'))
        // AsyncStorage.getItem('token').then(value => {
        //     authKey = value!;
        //     console.log(authKey);
        //
        //     axios.get(API_URL, {
        //             headers: {
        //                 Authorization: authKey,
        //                 'content-type': 'text/json'
        //             }
        //         }
        //     ).then(res => {
        //         roomsData = res.data;
        //         roomLabelElements = roomsData.map((room: any) => {
        //             return <RoomLabel
        //                 roomName={room.name}
        //                 characterData={room.characterData}
        //                 roomId={room.id}
        //             />
        //         });
        //     }).catch(err => {
        //         console.log("axios error: " + err);
        //     })
        //
        //
        // }).catch(err => {
        //     console.log("async storage error: " + err);
        // })

    })

    return (
        <View>
            <Text>
                BrowseRoomsScreen
            </Text>
            {roomLabelElements}
        </View>
    )
}
