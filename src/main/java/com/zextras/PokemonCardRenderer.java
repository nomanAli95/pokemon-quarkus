package com.zextras;

class PokemonCardRenderer {

    private PokemonCardRenderer() {}

    static String render(PokemonOverview p, PokemonSprites sprites) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8"/>
                  <title>#%d %s</title>
                  <style>
                    body { font-family: sans-serif; max-width: 400px; margin: 40px auto; text-align: center; background: #f5f5f5; }
                    h1 { text-transform: capitalize; }
                    .badge { background: #444; color: #fff; border-radius: 4px; padding: 2px 8px; margin: 2px; font-size: 0.85em; text-transform: capitalize; }
                    .sprite { image-rendering: pixelated; width: 96px; }
                    table { width: 100%%; border-collapse: collapse; margin-top: 16px; }
                    td { padding: 4px 8px; border-bottom: 1px solid #ddd; text-align: left; }
                    td:last-child { text-align: right; font-weight: bold; }
                  </style>
                </head>
                <body>
                  <h1>#%d %s</h1>
                  <div>%s</div>
                  <br/>
                  %s
                  %s
                  <table>
                    <tr><td>HP</td><td>%d</td></tr>
                    <tr><td>Attack</td><td>%d</td></tr>
                    <tr><td>Defense</td><td>%d</td></tr>
                    <tr><td>Sp. Attack</td><td>%d</td></tr>
                    <tr><td>Sp. Defense</td><td>%d</td></tr>
                    <tr><td>Speed</td><td>%d</td></tr>
                    <tr><td><strong>Total</strong></td><td>%d</td></tr>
                  </table>
                </body>
                </html>
                """.formatted(
                p.id, p.name,
                p.id, p.name,
                typeBadges(p.type1, p.type2),
                spritesSection(sprites, p.id, p.name),
                artworkSection(p.officialArtworkUrl, p.name),
                p.hp, p.attack, p.defense, p.spAttack, p.spDefense, p.speed, p.baseStatTotal
        );
    }

    private static String typeBadges(String type1, String type2) {
        String badges = "<span class=\"badge\">%s</span>".formatted(type1);
        if (type2 != null)
            badges += " <span class=\"badge\">%s</span>".formatted(type2);
        return badges;
    }

    private static String spritesSection(PokemonSprites sprites, int id, String name) {
        if (sprites == null) return "";

        String front = sprites.frontDefault != null
                ? "<img class=\"sprite\" src=\"/api/pokemon/%d/sprite\" alt=\"%s\"/>".formatted(id, name)
                : "";
        String shiny = sprites.frontShiny != null
                ? "<a href=\"/api/pokemon/%d/sprite/shiny\"><img class=\"sprite\" src=\"/api/pokemon/%d/sprite/shiny\" alt=\"%s shiny\"/></a>".formatted(id, id, name)
                : "";
        String back = sprites.backDefault != null
                ? "<img class=\"sprite\" src=\"/api/pokemon/%d/sprite/back\" alt=\"%s back\"/>".formatted(id, name)
                : "";

        if (front.isEmpty() && shiny.isEmpty() && back.isEmpty()) return "";
        return "<div>%s%s%s</div><br/>".formatted(front, shiny, back);
    }

    private static String artworkSection(String url, String name) {
        return url != null
                ? "<img src=\"%s\" width=\"250\" alt=\"%s official artwork\"/>".formatted(url, name)
                : "";
    }
}
