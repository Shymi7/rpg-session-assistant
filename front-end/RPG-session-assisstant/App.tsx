import {StatusBar} from 'expo-status-bar';
import {Text, View} from 'react-native';
import {NavigationContainer} from "@react-navigation/native";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import {LoginScreen} from "./Screens/LoginScreen";
import {NewCharacterScreen} from "./Screens/NewCharacterScreen";
import {NewRoomScreen} from "./Screens/NewRoomScreen";
import {CharacterSheetScreen} from "./Screens/CharacterSheetScreen";
import {GamemasterPanelScreen} from "./Screens/GamemasterPanelScreen";
import {SelectRoomScreen} from "./Screens/SelectRoomScreen";
import {SignInScreen} from "./Screens/SignInScreen";

const Stack = createNativeStackNavigator();

export default function App() {
    return (
        <NavigationContainer>
            <Stack.Navigator>
                <Stack.Screen name="login" component={LoginScreen} options={{title:'Log in'}}/>
                <Stack.Screen name="newCharacter" component={NewCharacterScreen} options={{title:'Create new character'}}/>
                <Stack.Screen name="newRoom" component={NewRoomScreen} options={{title:'Create new room'}}/>
                <Stack.Screen name="characterSheet" component={CharacterSheetScreen} />
                <Stack.Screen name="selectRoom" component={SelectRoomScreen} options={{title:'Your rooms'}}/>
                <Stack.Screen name="signIn" component={SignInScreen} options={{title:'Sign in'}}/>
                <Stack.Screen options={{}} name="gamemasterPanel" component={GamemasterPanelScreen} />
            </Stack.Navigator>
        </NavigationContainer>

    );
}

