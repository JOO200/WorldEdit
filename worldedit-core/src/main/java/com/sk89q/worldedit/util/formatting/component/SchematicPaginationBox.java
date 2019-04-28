package com.sk89q.worldedit.util.formatting.component;

import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.util.formatting.text.Component;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.event.ClickEvent;
import com.sk89q.worldedit.util.formatting.text.event.HoverEvent;
import com.sk89q.worldedit.util.formatting.text.format.TextColor;

import java.io.File;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

public class SchematicPaginationBox extends PaginationBox {
    private final String prefix;
    private final File[] files;

    public SchematicPaginationBox(String rootDir, File[] files, String pageCommand) {
        super("Available schematics", pageCommand);
        this.prefix = rootDir == null ? "" : rootDir;
        this.files = files;
    }

    @Override
    public Component getComponent(int number) {
        checkArgument(number < files.length - 1 && number >= 0);
        File file = files[number];
        Multimap<String, ClipboardFormat> exts = ClipboardFormats.getFileExtensionMap();
        String format = exts.get(Files.getFileExtension(file.getName()))
                .stream().findFirst().map(ClipboardFormat::getName).orElse("Unknown");
        boolean inRoot = file.getParentFile().getName().equals(prefix);

        String path = inRoot ? file.getName() : file.getPath().split(Pattern.quote(prefix + File.separator))[1];

        return TextComponent.builder()
                .content("")
                .append(TextComponent.of("[L]")
                        .color(TextColor.GOLD)
                        .clickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/schem load " + path))
                        .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of("Click to load"))))
                .append(Component.space())
                .append(TextComponent.of(path)
                        .color(TextColor.DARK_GREEN)
                        .hoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.of(format))))
                .build();
    }

    @Override
    public int getComponentsSize() {
        return files.length;
    }
}
