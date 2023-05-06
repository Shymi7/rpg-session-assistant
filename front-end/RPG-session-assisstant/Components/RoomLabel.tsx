import {Image, Text, TouchableOpacity, View} from "react-native";
import {Section} from "./Section";
import {useEffect, useState} from "react";
import {GETRequestWithAuthKey, getUserDataFromLocalStorage} from "../utils/utils";
import {API_URL} from "../env";

interface Props {
    roomName: string;
    roomId: number;
    isGM?: boolean;
}

export function RoomLabel({roomName, roomId, isGM = false}: Props) {

    const [characterData, setCharacterData] = useState<any>(null);

    useEffect(() => {
        getUserDataFromLocalStorage()
            .then(({authKey, playerId}) => {
                const getCharacterDataUrl = API_URL + '/api/room/' + roomId + '/character';
                GETRequestWithAuthKey(getCharacterDataUrl, authKey)
                    .then(res => {
                        setCharacterData(res.data);
                    })
                    .catch(err => {
                        console.log('get character data request error: '+err);
                    })
            })
            .catch(err => {
                console.log('get user data from async storage error: '+err);
            })

    }, [])

    function characterDataElement() {
        if (isGM)
            return (
                <Text className={'text-l text-color-white font-bold'}>
                    Gamemaster
                </Text>
            )

        if (characterData) {
            return (
                <View className={'flex-row'}>
                    <Text className={'text-l text-color-white pr-6'}>
                        {characterData.name}
                    </Text>
                    <Text className={'text-l text-color-white'}>
                        {'Lvl: '}
                    </Text>
                    <Text className={'text-l text-color-white font-bold'}>
                        {characterData.level}
                    </Text>
                </View>
            )
        }
        return (
            <></>
        )
    }


    return (
        <Section variant={isGM? 'light' : 'dark'} key={roomId}>
            <View className={'flex-row justify-around text-color-white h-20'}>
                <View className={'flex-col w-3/4 justify-around pl-3'}>
                    <Text className={'text-xl text-color-white'}>
                        {roomName}
                    </Text>
                    <Text className={'text-xl text-color-white'}>
                        {characterDataElement()}
                    </Text>
                </View>
                <TouchableOpacity
                    className={'w-1/4 bg-color-accent rounded-2xl overflow-hidden m-2 p-0 w-16 justify-center items-center'}
                >
                    <Image
                        source={require('../Icons/enterRoom.png')}
                        className={'w-10 h-12'}
                    />
                </TouchableOpacity>
            </View>
        </Section>
    )
}
