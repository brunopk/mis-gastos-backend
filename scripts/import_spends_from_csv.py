"""
Used to facilitate data migration from https://github.com/brunopk/mis-gastos to Mis Gastos Backend.
"""
import csv
import os

from datetime import datetime
from InquirerPy import inquirer

MIS_GASTOS_BEARER_TOKEN_ENV_VAR = "MIS_GASTOS_BEARER_TOKEN"
MIS_GASTOS_BASE_URL_ENV = "MIS_GASTOS_BASE_URL"
CSV_PATH_ENV = "CSV_PATH"

# TODO: create another scrip to process current spends (obtained from mis-gastos API) and change spend categories, subcategories and groups. User will be presented with spends, probably filtered by category, subcategory and group. For each one, he/she will decide if she/he wants to create a new mapping (similar as in CategorySubcategoryTupleMapper) or use an existing one to update this spend.

# TODO: add a script just to add and modify (enabling/disabling) categories

# TODO: create requirements.txt

class Spend:
    def __init__(
            self,
            date: datetime,
            origin: str,
            category: str,
            subcategory: str,
            description: str,
            account: str,
            value: str
    ):
        self.date = date
        self.origin = origin
        self.category = category
        self.subcategory = subcategory
        self.description = description
        self.account = account
        self.value = value

    def __str__(self):
        return f"""
            Date: {self.date}
            Category: {self.category}
            Subcategory: {self.subcategory}
            Description: {self.description}
            Account: {self.account}
            Value: {self.value}
        """


class Category:
    def __init__(self, id, name, subcategories):
        self.id = id
        self.name = name
        self.subcategories = subcategories

class Subcategory:
    def __init__(self, id, name):
        self.id = id
        self.name = name

class CategoryManager:
    """
    Used to create find or create categories in mis-gastos API.
    """

    # TODO: maintain a list of categories (ids and names), create a request builder that returns a function to invoke the corresponding endpoint to create a category. These functions will be invoked later, after all CSV is processed but before inserting the spends.

    def __init__(self):
        self._category_dict: dict[str, Category] = {
            "c1": Category("c1", "Cat 1", [Subcategory("c1_s1", "Cat 1 Subcat 1"), Subcategory("c1_s2", "Cat 1 Subcat 2")]),
            "c2": Category("c2", "Cat 2", [Subcategory("c2_s1", "Cat 1 Subcat 1")])
        }

    def category_dict(self):
        return { category_name: map_subcategory_list_to_str_list(self._category_dict[category_name].subcategories)
                 for category_name in self._category_dict.keys()}

class CategorySubcategoryMapper:
    """
    Categories and subcategories are mapped as tuples, this means that for example the
    ("Services", "Gas") tuple may be mapped to ("Other", "Other", "Other") even if the "Service" category maps to
    "Services (new)".
    """

    def __init__(self):
        self._tuple_dict = {}
        self._category_manager = CategoryManager()

    def map_category_subcategory_tuple(
            self,
            category_subcategory_tuple: tuple[str, str]
    ) -> tuple[int, int | None, int | None]:
        try:
            return self._tuple_dict[category_subcategory_tuple]
        except KeyError:
            pass

        # TODO: continue (2)
        self._create_new_category_subcategory_mapping()

        return 1, None, None

    def _create_new_category_subcategory_mapping(self):
        category_dict = self._category_manager.category_dict()

        # TODO: continue (1)

        cat = inquirer.select(
            message="Choose category:",
            choices=list(category_dict.keys())
        ).execute()

        subcat = inquirer.select(
            message="Choose subcategory:",
            choices=category_dict[cat]
        ).execute()

        print(cat, subcat)

def get_env(var_name):
    value = os.getenv(var_name)
    if value is None:
        raise Exception(f'Environment variable {var_name} not defined')
    return value

def map_subcategory_list_to_str_list(subcategory_list: list[Subcategory]):
    return [subcategory.name for subcategory in subcategory_list]

mis_gastos_base_url = get_env(MIS_GASTOS_BASE_URL_ENV)
if mis_gastos_base_url[-1] == '/':
    raise Exception(f'{mis_gastos_base_url} last character cannot be \'/\'')

google_login_url = f'{mis_gastos_base_url}/oauth2/authorization/google'

bearer_token = input(f'Bearer token (open {google_login_url} in browser and follow instructions to obtain it): ')

csv_file_path = get_env(CSV_PATH_ENV)

# TODO: try to do a request with token

category_subcategory_mapper = CategorySubcategoryMapper()

with open(csv_file_path, 'r') as csv_file:
    csv_reader = csv.reader(csv_file)
    header = next(csv_reader)
    for row in csv_reader:
        spend = Spend(
            datetime.strptime(row[1].strip(), "%d/%m/%Y"),
            row[2].strip(),
            row[3].strip(),
            row[4].strip(),
            row[5].strip(),
            row[6].strip(),
            row[7].strip()
        )

        print('\n\nItem found with a (category, subcategory) tuple that has not been mapped yet.')
        print('\nCurrent item being processed :')
        print(f'{spend}')

        category_subcategory_mapper.map_category_subcategory_tuple((spend.category, spend.subcategory))

        # TODO: continue (3) use category_subcategory_tuple_mapper

        print("-------------------------------------------------------------------------------------------------------")
