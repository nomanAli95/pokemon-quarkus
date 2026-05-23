package com.zextras.pokemon.renderer;

import com.zextras.pokemon.dto.PokemonBasicCard;
import com.zextras.pokemon.model.PokemonOverview;
import com.zextras.pokemon.model.PokemonSprites;

public class PokemonBasicCardRenderer {

  private PokemonBasicCardRenderer() {}

  public static String render(PokemonBasicCard card) {
    PokemonOverview p = card.overview();
    PokemonSprites sprites = card.sprites();

    return "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n"
        + "  <meta charset=\"UTF-8\"/>\n"
        + "  <title>#" + String.format("%03d", p.id) + " " + p.name + "</title>\n"
        + "  <style>\n"
        + "    body{font-family:system-ui,sans-serif;background:#f5f5f5;padding:24px}\n"
        + "    .card{max-width:400px;margin:0 auto;background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 2px 12px rgba(0,0,0,.12)}\n"
        + "    .header{background:#5c6bc0;color:#fff;padding:20px;text-align:center}\n"
        + "    .header h1{font-size:1.6em;text-transform:capitalize;margin:4px 0}\n"
        + "    .num{opacity:.7;font-size:.9em}\n"
        + "    .types{display:flex;gap:6px;justify-content:center;margin-top:8px}\n"
        + "    .type{border-radius:20px;padding:3px 12px;font-size:.78em;font-weight:600;color:#fff;text-transform:capitalize}\n"
        + "    .type-grass{background:#78C850}.type-fire{background:#F08030}.type-water{background:#6890F0}\n"
        + "    .type-poison{background:#A040A0}.type-electric{background:#F8D030;color:#333}\n"
        + "    .type-psychic{background:#F85888}.type-ice{background:#98D8D8;color:#333}\n"
        + "    .type-dragon{background:#7038F8}.type-dark{background:#705848}\n"
        + "    .type-fairy{background:#EE99AC;color:#333}.type-normal{background:#A8A878;color:#333}\n"
        + "    .type-fighting{background:#C03028}.type-flying{background:#A890F0}\n"
        + "    .type-bug{background:#A8B820;color:#333}.type-rock{background:#B8A038}\n"
        + "    .type-ghost{background:#705898}.type-ground{background:#E0C068;color:#333}\n"
        + "    .type-steel{background:#B8B8D0;color:#333}\n"
        + "    .artwork{text-align:center;padding:16px;background:#fafafa;border-bottom:1px solid #eee}\n"
        + "    .artwork img{width:200px}\n"
        + "    .sprites{display:flex;gap:4px;justify-content:center;padding:10px;background:#f0f0f0;border-bottom:1px solid #eee}\n"
        + "    .sprite{image-rendering:pixelated;width:80px}\n"
        + "    .stats{padding:16px 20px}\n"
        + "    .stats h2{font-size:.72em;text-transform:uppercase;letter-spacing:.08em;color:#999;margin-bottom:10px}\n"
        + "    .stat{display:flex;align-items:center;gap:8px;margin-bottom:6px;font-size:.84em}\n"
        + "    .stat-name{width:80px;color:#888;text-align:right;flex-shrink:0}\n"
        + "    .stat-val{width:28px;font-weight:700;text-align:right;flex-shrink:0}\n"
        + "    .bar-bg{flex:1;background:#eee;border-radius:4px;height:7px}\n"
        + "    .bar{height:7px;border-radius:4px}\n"
        + "    .full-link{display:block;text-align:center;padding:12px;background:#f0f0ff;color:#5c6bc0;font-size:.85em;font-weight:600;text-decoration:none;border-top:1px solid #e0e0ff}\n"
        + "    .full-link:hover{background:#e8eaf6}\n"
        + "  </style>\n</head>\n<body>\n"
        + "  <div class=\"card\">\n"
        + "    <div class=\"header\">\n"
        + "      <div class=\"num\">#" + String.format("%03d", p.id) + "</div>\n"
        + "      <h1>" + p.name + "</h1>\n"
        + "      <div class=\"types\">" + typeSpan(p.type1) + (p.type2 != null ? typeSpan(p.type2) : "") + "</div>\n"
        + "    </div>\n"
        + artworkSection(p.officialArtworkUrl, p.name)
        + spritesSection(sprites, p.id, p.name)
        + "    <div class=\"stats\"><h2>Base Stats</h2>\n"
        + statRow("HP", p.hp)
        + statRow("Attack", p.attack)
        + statRow("Defense", p.defense)
        + statRow("Sp. Atk", p.spAttack)
        + statRow("Sp. Def", p.spDefense)
        + statRow("Speed", p.speed)
        + statRow("Total", p.baseStatTotal)
        + "    </div>\n"
        + "    <a class=\"full-link\" href=\"/api/pokemon/" + p.id + "/card/full\">View full card →</a>\n"
        + "  </div>\n</body>\n</html>\n";
  }

  private static String typeSpan(String type) {
    return "<span class=\"type type-" + type + "\">" + type + "</span>";
  }

  private static String artworkSection(String url, String name) {
    if (url == null) return "";
    return "    <div class=\"artwork\"><img src=\"" + url + "\" alt=\"" + name + " official artwork\"/></div>\n";
  }

  private static String spritesSection(PokemonSprites sprites, int id, String name) {
    if (sprites == null) return "";
    StringBuilder sb = new StringBuilder();
    if (sprites.frontDefault != null)
      sb.append("<img class=\"sprite\" src=\"/api/pokemon/").append(id).append("/sprite\" alt=\"").append(name).append("\"/>");
    if (sprites.frontShiny != null)
      sb.append("<a href=\"/api/pokemon/").append(id).append("/sprite/shiny\">")
        .append("<img class=\"sprite\" src=\"/api/pokemon/").append(id).append("/sprite/shiny\" alt=\"").append(name).append(" shiny\"/>")
        .append("</a>");
    if (sprites.backDefault != null)
      sb.append("<img class=\"sprite\" src=\"/api/pokemon/").append(id).append("/sprite/back\" alt=\"").append(name).append(" back\"/>");
    if (sb.isEmpty()) return "";
    return "    <div class=\"sprites\">" + sb + "</div>\n";
  }

  private static String statRow(String name, Integer value) {
    if (value == null) return "";
    int max = name.equals("Total") ? 800 : 255;
    int pct = (int) Math.min(100, (value / (double) max) * 100);
    String color = name.equals("Total") ? "#7c4dff"
        : value >= 120 ? "#2196F3" : value >= 90 ? "#4CAF50" : value >= 50 ? "#FF9800" : "#f44336";
    return "      <div class=\"stat\">"
        + "<span class=\"stat-name\">" + name + "</span>"
        + "<span class=\"stat-val\">" + value + "</span>"
        + "<div class=\"bar-bg\"><div class=\"bar\" style=\"width:" + pct + "%;background:" + color + "\"></div></div>"
        + "</div>\n";
  }
}
