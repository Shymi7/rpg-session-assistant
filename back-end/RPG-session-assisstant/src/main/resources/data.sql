-- Player Entity
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (1, 'lil_NAS', 'password');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (2, 'Gordon_Freeman', 'password');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (3, 'gandalf_from_narnia', 'password123');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (4, 'Jerzyzkoszalina', 'password');
-- Gamemaster Entity
INSERT INTO zpsm_projekt.Gamemaster (gamemaster_id, player_id)
VALUES (1, 3);
-- Character Entity
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience)
VALUES (1, 1, 'lil_NASARIUS', 10, 100, 3, 500);
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience)
VALUES (2, 2, 'cagedman', 10, 100, 3, 500);
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience)
VALUES (3, 4, 'koszalin_sorcerer', 10, 100, 3, 500);
-- Attribute Entity
---- Character Attributes
INSERT INTO zpsm_projekt.Attribute (attribute_id, name)
VALUES (1, 'Agility');
INSERT INTO zpsm_projekt.Attribute (attribute_id, name)
VALUES (2, 'Intelligence');
INSERT INTO zpsm_projekt.Attribute (attribute_id, name)
VALUES (3, 'Charisma');
INSERT INTO zpsm_projekt.Attribute (attribute_id, name)
VALUES (4, 'Strength');
---- Item Attributes
INSERT INTO zpsm_projekt.Attribute (attribute_id, name)
VALUES (5, 'Weight');
INSERT INTO zpsm_projekt.Attribute (attribute_id, name)
VALUES (6, 'Price');
-- Item Entity
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (1, 'Ulu-Mulu',
        'Posiadający tę broń zdobywa szacunek orków i może przechodzić obok nich bez obawy ataku z ich strony.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (2, 'RTX 4090',
        'Nakłada na przeciwnika efekt szoku po zobaczeniu ceny i wymiarów karty.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (3, 'GTA Vice City',
        'Nakłada na przeciwnika efekt nostalgii i traci on chęć do walki.');
-- Item_Character Entity
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (1, 1);
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (2, 2);
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (3, 2);
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (3, 3);
-- Item_Attribute Entity
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (5, 1, 30);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (6, 1, 300);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (5, 2, 4090);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (6, 2, 1599);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (5, 3, 3);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (6, 3, 49);
-- Character_Attribute Entity
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (1, 1, 5);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (2, 1, 3);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (3, 1, 3);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (4, 1, 4);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (1, 2, 3);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (2, 2, 2);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (3, 2, 1);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (4, 2, 4);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (1, 3, 2);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (2, 3, 4);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (3, 3, 5);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (4, 3, 5);
-- Room Entity
INSERT INTO zpsm_projekt.Room (room_id, gamemaster_id, capacity, password, NAME)
VALUES (1, 1, 6, '123qwe', 'Ohio');
-- Room_Character Entity
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (1, 1);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (1, 2);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (1, 3);
-- Quest Entity
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (1, 'Poza znanym...', 'Twoja przygoda w tym zapomnianym przez bogów miejscu właśnie się zaczyna.');
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (2, 'Dziad rzepny', 'Zbierz 20 rzep dla Dziada.');
-- Quest_Character Entity
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (1, 1);
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (1, 2);
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (2, 3);
