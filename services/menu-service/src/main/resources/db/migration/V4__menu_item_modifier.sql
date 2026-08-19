CREATE TABLE menu_item_modifier(
    menu_item_id BIGINT NOT NULL,
    modifier_id BIGINT NOT NULL,
    PRIMARY KEY(menu_item_id, modifier_id),
    CONSTRAINT fk_mim_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_item(id) ON DELETE CASCADE,
    CONSTRAINT fk_mim_modifier FOREIGN KEY (modifier_id) REFERENCES modifier(id) ON DELETE CASCADE
)