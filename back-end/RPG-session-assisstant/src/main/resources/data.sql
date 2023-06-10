-- Player Entity
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (1, 'Zosia', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (2, 'Staszek', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (3, 'Wandzia', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (4, 'Krzysiek', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');
INSERT INTO zpsm_projekt.Player (player_id, login, password)
VALUES (5, 'Cela', '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a');

-- Gamemaster Entity
INSERT INTO zpsm_projekt.Gamemaster (gamemaster_id, player_id)
VALUES (1, 2);
INSERT INTO zpsm_projekt.Gamemaster (gamemaster_id, player_id)
VALUES (2, 3);
INSERT INTO zpsm_projekt.Gamemaster (gamemaster_id, player_id)
VALUES (3, 4);
INSERT INTO zpsm_projekt.Gamemaster (gamemaster_id, player_id)
VALUES (4, 5);


-- Character Entity
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (1, 1, 'Gandalf', 21, 200, 6, 87, 'A wise and enigmatic wizard, Gandalf wields immense power and knowledge of ancient magic. With his long flowing beard and staff in hand, he is a formidable ally and a force to be reckoned with in the realms of Middle-earth.');
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (2, 3, 'Harry Potter', 12, 130, 2, 21, 'You are a wizard, Harry, destined to battle the forces of darkness. Armed with a wand and accompanied by loyal friends, you embark on a thrilling journey to discover your true potential as a wizard and confront the dark lord Voldemort.');
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (3, 4, 'Jack Sparrow', 22, 80, 8, 54, 'Captain Jack Sparrow, the infamous pirate with a charm for trouble and a knack for getting out of it. With his quick wit, and cunning strategies, he sails through treacherous waters in search of adventure, hidden treasures, and a sip of rum.');
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (4, 1, 'Luke Skywalker', 18, 150, 5, 65, 'A skilled Jedi Knight and the last hope of the galaxy in the Star Wars saga. Luke possesses a strong connection to the Force and wields a lightsaber in his quest to defeat the Sith and restore peace.');
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (5, 1, 'Hermione Granger', 15, 120, 4, 40, 'A brilliant and resourceful witch from the Harry Potter series. Hermione is known for her extensive knowledge of magic and her quick thinking. With her wand and clever spellcasting, she aids Harry in his fight against dark forces.');
INSERT INTO zpsm_projekt.Character (character_id, char_player_id, name, level, health, skill_points, experience, description)
VALUES (6, 1, 'Davy Jones', 25, 180, 7, 95, 'A skilled ranger and the rightful heir to the throne in The Lord of the Rings trilogy. Aragorn is a skilled swordsman, leading the Fellowship on their perilous journey to destroy the One Ring and save Middle-earth.');

-- Room Entity
INSERT INTO zpsm_projekt.Room (room_id, gamemaster_id, capacity, password, NAME)
VALUES (1, 1, 6, '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a', 'Gondor');
INSERT INTO zpsm_projekt.Room (room_id, gamemaster_id, capacity, password, NAME)
VALUES (2, 2, 4, '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a', 'Tatooine');
INSERT INTO zpsm_projekt.Room (room_id, gamemaster_id, capacity, password, NAME)
VALUES (3, 3, 5, '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a', 'Hogwarts');
INSERT INTO zpsm_projekt.Room (room_id, gamemaster_id, capacity, password, NAME)
VALUES (4, 4, 5, '$2a$12$DvyXGZq9v.bIOmofMmCBcOLTs3k925PrJc3DwqcY.9yJKR9i2oC9a', 'Caribbean');

-- Room_Character Entity
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (1, 1);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (1, 2);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (1, 3);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (2, 4);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (3, 5);
INSERT INTO zpsm_projekt.Room_Character (room_id, character_id)
VALUES (4, 6);


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
VALUES (1, 'Shadowblade',
        'A weapon that commands respect from orcs, allowing its wielder to pass by them without fear of attack.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (2, 'Dragonfire Staff',
        'Inflicts a shock effect on opponents upon witnessing the immense power and size of the enchanted staff.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (3, 'Scroll of Arcane Wisdom',
        'Imbues opponents with a sense of nostalgia and diminishes their desire to engage in combat.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (4, 'Soulbane',
        'A legendary magical blade capable of cleaving through even the toughest steel armor.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (5, 'Elixir of Vitality',
        'A potion that restores health and accelerates the healing of wounds.');
INSERT INTO zpsm_projekt.Item (item_id, name, description)
VALUES (6, 'Veil of Concealment',
        'A cloak that grants invisibility to the wearer, rendering them unseen by others.');




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
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (1, 4, 3);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (2, 4, 4);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (3, 4, 2);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (4, 4, 5);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (1, 5, 5);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (2, 5, 3);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (3, 5, 4);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (4, 5, 1);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (1, 6, 2);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (2, 6, 4);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (3, 6, 5);
INSERT INTO zpsm_projekt.Character_Attribute (attribute_id, character_id, attribute_level)
VALUES (4, 6, 3);


-- Quest Entity
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (1, 'Beyond the Known...','Your adventure in this forsaken place, abandoned by the gods, is about to begin.');
INSERT INTO zpsm_projekt.Quest (quest_id, name, description)
VALUES (2, 'The Elder Gourd','Gather 20 gourds for the Elder Gourd.');
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