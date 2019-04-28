/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.util.formatting.component;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.util.formatting.text.Component;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.event.ClickEvent;
import com.sk89q.worldedit.util.formatting.text.event.HoverEvent;
import com.sk89q.worldedit.util.formatting.text.format.TextColor;

import java.util.List;

public class CommandListBox extends PaginationBox {

    private List<CommandEntry> commands = Lists.newArrayList();

    /**
     * Create a new box.
     *
     * @param title the title
     */
    public CommandListBox(String title, String pageCommand) {
        super(title, pageCommand);
    }

    @Override
    public Component getComponent(int number) {
        return commands.get(number).createComponent();
    }

    @Override
    public int getComponentsSize() {
        return commands.size();
    }

    public void appendCommand(String alias, Component description) {
        appendCommand(alias, description, null);
    }

    public void appendCommand(String alias, String description, String insertion) {
        appendCommand(alias, TextComponent.of(description), insertion);
    }

    public void appendCommand(String alias, Component description, String insertion) {
        commands.add(new CommandEntry(alias, description, insertion));
    }

    private static class CommandEntry {
        private final String alias;
        private final Component description;
        private final String insertion;

        CommandEntry(String alias, Component description, String insertion) {
            this.alias = alias;
            this.description = description;
            this.insertion = insertion;
        }

        Component createComponent() {
            TextComponentProducer line = new TextComponentProducer();
            line.append(SubtleFormat.wrap("? ")
                    .clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "//help " + insertion))
                    .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of("Additional Help"))));
            TextComponent command = TextComponent.of(alias, TextColor.GOLD);
            if (insertion == null) {
                line.append(command);
            } else {
                line.append(command
                        .clickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, insertion))
                        .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of("Click to select"))));
            }
            return line.append(TextComponent.of(": ")).append(description).create();
        }
    }
}
