import React, {useEffect} from "react";
import {View} from "react-native";
import {CharacterData} from "../utils/CharacterData";
import {GETRequestWithAuthKey, getUserDataFromLocalStorage} from "../utils/utils";
import {API_URL} from "../env";
import {CharacterInfo} from "../Components/CharacterInfo";

export function GamemasterPanelScreen({route, navigation}: any) {

    const [charactersList, setCharacterList] = React.useState();
    const [currentCharacterData, setCurrentCharacterData] = React.useState<CharacterData>();
    const [isCharacterListVisible, setIsCharacterListVisible] = React.useState(false);

    let authKey: string | null;
    let playerId: string | null;

    async function getCharacterList() {
        try {
            const playerData = await getUserDataFromLocalStorage()
            authKey = playerData.authKey;
            playerId = playerData.playerId;

            const getCharacterDataUrl = API_URL + '/api/room/' + route.params.roomId + '/characters';
            const res = await GETRequestWithAuthKey(getCharacterDataUrl, authKey)

            setCharacterList(res.data);
            setCurrentCharacterData(res.data[0]);//default character is the first character

        } catch (error) {
            console.log('get characters list request error: ' + error);
        }
    }

    useEffect(() => {
        getCharacterList()
            .catch(error => console.error(error));
    }, [])


    return (
        <View>
            {
                currentCharacterData &&
                <CharacterInfo
                    name={currentCharacterData.name}
                    description={currentCharacterData.description}
                    level={currentCharacterData.level}
                    experience={currentCharacterData.experience}
                    GMMode={true}
                    showCharacterListFunc={() => {
                        setIsCharacterListVisible(prevState => !prevState);
                    }}
                />
            }

        </View>
    );
}
