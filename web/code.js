/* jshint esversion: 6 */

document.getElementById('searchbutton').onclick = () => {
    fetch("/search?q=" + document.getElementById('searchbox').value)
    .then((response) => response.json())
    .then((data) => {
        // Check if the message key exists in the response
        if (data[0] && data[0].message) {
            document.getElementById("responsesize").innerHTML = 
                `<p>${data[0].message}</p>`;
        } else {
            document.getElementById("responsesize").innerHTML = 
                `<p>${data.length} websites retrieved</p>`;
        }

        let results = data.map((page) =>
            `<li><a href="${page.url}">${page.title}</a></li>`)
            .join("\n");
        document.getElementById("urllist").innerHTML = `<ul>${results}</ul>`;
    });
};
