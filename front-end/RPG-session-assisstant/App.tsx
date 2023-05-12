import {NavigationContainer} from "@react-navigation/native";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import {LoginScreen} from "./Screens/LoginScreen";
import {EnterNewRoomScreen} from "./Screens/EnterNewRoomScreen";
import {CreateNewRoomScreen} from "./Screens/CreateNewRoomScreen";
import {CharacterSheetScreen} from "./Screens/CharacterSheetScreen";
import {GamemasterPanelScreen} from "./Screens/GamemasterPanelScreen";
import {RegisterScreen} from "./Screens/RegisterScreen";
import {BrowseRoomsScreen} from "./Screens/BrowseRoomsScreen";

const Stack = createNativeStackNavigator();

export default function App() {
    return (
        <NavigationContainer>
            <Stack.Navigator>

                <Stack.Screen name="login"              component={LoginScreen}             options={{title:'Log in'}}/>
                <Stack.Screen name="register"           component={RegisterScreen}          options={{title:'Sign up'}}/>

                <Stack.Screen name="browseRooms"        component={BrowseRoomsScreen}       options={{title:'Browse rooms'}}/>

                <Stack.Screen name="createNewRoom"      component={CreateNewRoomScreen}     options={{title:'Create new room'}}/>
                <Stack.Screen name="enterNewRoom"       component={EnterNewRoomScreen}      options={{title:'Enter new room'}}/>

                <Stack.Screen name="characterSheet"     component={CharacterSheetScreen}    options={{}}/>
                <Stack.Screen name="gamemasterPanel"    component={GamemasterPanelScreen}   options={{}}/>

            </Stack.Navigator>
        </NavigationContainer>

    );
}

