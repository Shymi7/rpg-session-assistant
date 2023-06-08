import React, {useEffect} from "react";
import {ScrollView} from "react-native";
import {GETRequestWithAuthKey, getUserDataFromLocalStorage} from "../utils/utils";
import {API_URL} from "../env";
import {CharacterData} from "../utils/CharacterData";
import {CharacterInfo} from "../Components/CharacterInfo";
import {CharacterAttributes} from "../Components/CharacterAttributes";
import {CharacterQuests} from "../Components/CharacterQuests";
import {CharacterInventory} from "../Components/CharacterInventory";


export function CharacterSheetScreen({route, navigation}: any) {

    const [characterData, setCharacterData] = React.useState<CharacterData>();
    const [refreshSwitch, setRefreshSwitch] = React.useState<boolean>(false);


    async function getData() {
        try {
            const {authKey, playerId} = await getUserDataFromLocalStorage()

            const getCharacterDataUrl = API_URL + '/api/room/' + route.params.roomId + '/character';
            const res = await GETRequestWithAuthKey(getCharacterDataUrl, authKey)

            setCharacterData(res.data);
        } catch (error) {
            console.log('add XP to character request error: ' + error);
        }
    }


    useEffect(() => {
        getData()
            .catch(error => console.error(error));
    }, [refreshSwitch])


    function refreshComponent() {
        setRefreshSwitch(prevState => !prevState);
    }

    return (
        characterData &&
        <ScrollView>
            <CharacterInfo
                name={characterData.name}
                description={characterData.description}
                level={characterData.level}
                experience={characterData.experience}
                characterId={characterData.id}
                refreshFunc={refreshComponent}
                roomID={route.params.roomId}
            />
            <CharacterAttributes
                attributesList={characterData.characterAttributes}
                freeSkillPoints={characterData.skillPoints}
                refreshFunc={getData}
                characterId={characterData.id}
            />
            <CharacterQuests
                questsList={characterData.quests}
            />
            <CharacterInventory
                itemsList={characterData.items}
                refreshFunc={refreshComponent}
            />
        </ScrollView>
    );


}
