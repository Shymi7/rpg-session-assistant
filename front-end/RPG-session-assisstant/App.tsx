import {StatusBar} from 'expo-status-bar';
import {Text, View} from 'react-native';
import {NavigationContainer} from "@react-navigation/native";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import {LoginScreen} from "./Screens/LoginScreen";
import {NewCharacterScreen} from "./Screens/NewCharacterScreen";
import {NewRoomScreen} from "./Screens/NewRoomScreen";
import {CharacterSheetScreen} from "./Screens/CharacterSheetScreen";
import {GamemasterPanelScreen} from "./Screens/GamemasterPanelScreen";

const Stack = createNativeStackNavigator();

export default function App() {
    return (
        <NavigationContainer>
            <Stack.Navigator>
                <Stack.Screen name="login" component={LoginScreen} options={{title:'Connect to room'}}/>
                <Stack.Screen name="newCharacter" component={NewCharacterScreen} />
                <Stack.Screen name="newRoom" component={NewRoomScreen} />
                <Stack.Screen name="characterSheet" component={CharacterSheetScreen} />
                <Stack.Screen options={{}} name="gamemasterPanel" component={GamemasterPanelScreen} />
            </Stack.Navigator>
        </NavigationContainer>

    );
}

