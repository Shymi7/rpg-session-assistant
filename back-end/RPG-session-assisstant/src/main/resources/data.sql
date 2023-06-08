-- Player Entity
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (1, 'Zosia', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (2, 'Staszek', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (3, 'Wandzia', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (4, 'Krzysiek', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');

-- Gamemaster Entity
INSERT INTO zpsm_projekt.Gamemaster (gamemaster_id, player_id)
VALUES (1, 2);
INSERT INTO zpsm_projekt.Gamemaster (gamemaster_id, player_id)
VALUES (2, 3);
INSERT INTO zpsm_projekt.Gamemaster (gamemaster_id, player_id)
VALUES (3, 4);


-- Character Entity
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (1, 1, 'Gandalf', 21, 200, 6, 87, 'Czarodziej');
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (2, 1, 'Harry Potter', 12, 130, 2, 21, 'You are squatter Harry');
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (3, 1, 'Jack Sparrow', 22, 80, 8, 54, 'Tpb.com');


-- Room Entity
INSERT INTO zpsm_projekt.Room (room_id, gamemaster_id, capacity, password, NAME)
VALUES (1, 1, 6, '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a', 'Gondor');
INSERT INTO zpsm_projekt.Room (room_id, gamemaster_id, capacity, password, NAME)
VALUES (2, 2, 4, '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a', 'Hogwarts');
INSERT INTO zpsm_projekt.Room (room_id, gamemaster_id, capacity, password, NAME)
VALUES (3, 3, 5, '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a', 'Mordor');

-- Room_Character Entity
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (1, 1);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (2, 2);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (3, 3);


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
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (4, 'Excalibur',
        'Legendarna magiczna klinga, która może przeciąć stalowy pancerz.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (5, 'Potion of Healing',
        'Mikstura, która przywraca zdrowie i regeneruje rany.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (6, 'Invisibility Cloak',
        'Peleryna, która sprawia, że noszący ją staje się niewidzialny dla innych.');

-- Item_Character Entity
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (1, 1);
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (2, 1);
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (3, 1);
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (4, 1);
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (5, 1);
INSERT INTO zpsm_projekt.Item_Character (item_id, character_id)
VALUES (6, 1);


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
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (5, 4, 50);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (6, 4, 500);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (5, 5, 1);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (6, 5, 10);
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (5, 6, 3); -- No specific attribute value for the Invisibility Cloak
INSERT INTO zpsm_projekt.Item_Attribute (attribute_id, item_id, attribute_value)
VALUES (6, 6, 7312);

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

-- Quest Entity
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (1, 'Poza znanym...', 'Twoja przygoda w tym zapomnianym przez bogów miejscu właśnie się zaczyna.');
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (2, 'Dziad rzepny', 'Zbierz 20 rzep dla Dziada.');
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (3, 'The Lost Artifact', 'Embark on a journey to find the ancient artifact and save the world.');
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (4, 'The Forbidden Temple', 'Explore the mysterious temple and uncover its hidden secrets.');
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (5, 'The Enchanted Forest', 'Embark on a quest to explore the mystical Enchanted Forest.');
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (6, 'The Haunted Manor', 'Investigate the eerie Haunted Manor and unravel its ghostly secrets.');
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (7, 'The Ancient Ruins', 'Discover the secrets of the long-lost Ancient Ruins and uncover hidden treasures.');
-- Quest_Character Entity
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (1, 1);
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (2, 1);
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (3, 1);
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (4, 1);
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (5, 1);
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (6, 1);
INSERT INTO zpsm_projekt.Quest_Character (quest_id, character_id)
VALUES (7, 1);