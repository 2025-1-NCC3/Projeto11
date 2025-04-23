const shift = 3;

function isBasicLatinLetter(char) {
  const code = char.charCodeAt(0);
  return (
    (code >= 65 && code <= 90) || // A-Z
    (code >= 97 && code <= 122)
  ); // a-z
}

function descriptografarCesar(str) {
  if (!str) return str;

  return str
    .split("")
    .map((char) => {
      const charCode = char.charCodeAt(0);
      if (char >= "a" && char <= "z") {
        return String.fromCharCode(((charCode - 97 - shift + 26) % 26) + 97);
      } else if (char >= "A" && char <= "Z") {
        return String.fromCharCode(((charCode - 65 - shift + 26) % 26) + 65);
      } else if (char >= "0" && char <= "9") {
        return String.fromCharCode(((charCode - 48 - shift + 10) % 10) + 48);
      } else {
        return char; // Mantém caracteres especiais inalterados
      }
    })
    .join("");
}

function criptografarCesar(str) {
  if (!str) return str;

  return str
    .split("")
    .map((char) => {
      const charCode = char.charCodeAt(0);
      if (char >= "a" && char <= "z") {
        return String.fromCharCode(((charCode - 97 + shift) % 26) + 97);
      } else if (char >= "A" && char <= "Z") {
        return String.fromCharCode(((charCode - 65 + shift) % 26) + 65);
      } else if (char >= "0" && char <= "9") {
        return String.fromCharCode(((charCode - 48 + shift) % 10) + 48);
      } else {
        return char; // Mantém caracteres especiais inalterados
      }
    })
    .join("");
}

module.exports = {
  descriptografar: descriptografarCesar,
  criptografar: criptografarCesar,
};