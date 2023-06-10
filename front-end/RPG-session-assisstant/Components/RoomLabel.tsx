import {Image, Text, TouchableOpacity, View} from "react-native";
import {Section} from "./Section";
import {useEffect, useState} from "react";
import {GETRequestWithAuthKey, getUserDataFromLocalStorage} from "../utils/utils";
import {API_URL} from "../env";

interface Props {
    roomName: string;
    roomId: number;
    isGM?: boolean;
    navigation: any;
}

export function RoomLabel({roomName, roomId, isGM = false, navigation}: Props) {

    const [characterData, setCharacterData] = useState<any>(null);

    async function getDataFromApi() {
        try{
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            const getCharacterDataUrl = API_URL + '/api/room/' + roomId + '/character';
            const res = await GETRequestWithAuthKey(getCharacterDataUrl, authKey)

            setCharacterData(res.data);
        } catch (error){
            console.log('get character data request error: ' + error);
        }

    }

    useEffect(() => {
        if(!isGM){
            getDataFromApi()
                .catch(error => console.error(error));
        }

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
        <Section colorVariant={isGM ? 'light' : 'dark'}>
            <View className={'flex-row justify-around text-color-white h-20'}>
                <View className={'flex-col w-3/4 justify-around '}>
                    <Text className={'text-xl text-color-white'}>
                        {roomName}
                    </Text>
                    <Text className={'text-xl text-color-white'}>
                        {characterDataElement()}
                    </Text>
                </View>

                <TouchableOpacity
                    className={'w-fit bg-color-accent rounded-2xl overflow-hidden p-0 w-20 justify-center items-center'}
                    onPress={() => {
                        if(isGM){
                            navigation.navigate('gamemasterPanel', {roomId: roomId})
                        } else {
                            navigation.navigate('characterSheet', {roomId: roomId})
                        }
                    }}
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
